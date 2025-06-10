import React, { useEffect, useState } from 'react';

function BookStudyPosts() {
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const BACKEND_URL = "http://localhost:8080";

  useEffect(() => {
    // 데이터 불러오기
    fetch('/api/bookstudy')
      .then((res) => {
        if (!res.ok) throw new Error('네트워크 오류');
        return res.json();
      })
      .then((data) => {
        setPosts(data);
        setLoading(false);
      })
      .catch((err) => {
        setError('책 공부 게시글 데이터를 불러오는 중 오류가 발생했습니다.');
        setLoading(false);
      });
  }, []);

  if (loading) return <div>로딩 중...</div>;
  if (error) return <div>{error}</div>;
  if (posts.length === 0) return <div>등록된 책 공부 게시글이 없습니다.</div>;

  return (
    <div id="bookstudy-box">
      <h3>📌 책 공부 게시글 목록</h3>
      <button id="bookStudy_writeBtn">책 공부 게시글 작성하기</button>
      {posts.map((post) => (
        <div
          className="card"
          key={post.id}
          style={{ cursor: 'pointer', marginBottom: '1rem' }}
          onClick={() => window.location.href = `BookStudyDetail?id=${post.id}`}
        >
          <h3>페이지 번호: {post.pageNumber}</h3>
          <p>{post.contents.length > 100 ? post.contents.substring(0, 100) + '...' : post.contents}</p>
          <img src={`${BACKEND_URL}${post.imagePath}`} alt="책 공부 이미지" style={{ maxWidth: '200px' }} />
        </div>
      ))}
    </div>
  );
}

export default BookStudyPosts;