import React, { useEffect, useState } from 'react';

const BookStudy_detail = () => {
  const [post, setPost] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const BACKEND_URL = "http://localhost:8080";


  // 쿼리스트링에서 id 추출
  const getPostId = () => {
    const params = new URLSearchParams(window.location.search);
    return params.get('id');
  };

  useEffect(() => {
    const postId = getPostId();
    if (!postId) {
      setError('잘못된 접근입니다.');
      setLoading(false);
      return;
    }

    fetch(`/api/bookstudy/${postId}`)
      .then((res) => {
        if (!res.ok) throw new Error('네트워크 오류');
        return res.json();
      })
      .then((data) => {
        setPost(data);
        setLoading(false);
      })
      .catch(() => {
        setError('책 공부 게시글 정보를 불러오는 중 오류가 발생했습니다.');
        setLoading(false);
      });
  }, []);

  const handleDelete = () => {
    const postId = getPostId();
    if (!postId) return;

    if (window.confirm('정말로 이 게시글을 삭제하시겠습니까?')) {
      fetch(`/api/bookstudy/${postId}`, { method: 'DELETE' })
        .then((res) => {
          if (!res.ok) throw new Error('삭제 실패');
          alert('게시글이 삭제되었습니다.');
          window.location.href = 'index.html';
        })
        .catch(() => {
          alert('삭제 중 오류가 발생했습니다.');
        });
    }
  };

  if (loading) return <div>로딩 중...</div>;
  if (error) return <div>{error}</div>;
  if (!post) return null;

  return (
    <div className="bookstudy-detail">
      <h2>책 공부 게시글 상세</h2>
      <div>
        <strong>페이지 번호:</strong> <span id="post-pageNumber">{post.pageNumber}</span>
      </div>
      <div>
        <strong>내용:</strong>
        <p id="post-contents">{post.contents}</p>
      </div>
      {post.imagePath && (
        <div>
         <img src={`${BACKEND_URL}${post.imagePath}`} alt="책 공부 이미지" style={{ maxWidth: '400px' }} />
        </div>
      )}
      <button id="deleteBtn" onClick={handleDelete}>삭제</button>
    </div>
  );
};

export default BookStudy_detail;