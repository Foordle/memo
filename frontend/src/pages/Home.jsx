import React from 'react'
import { Link } from 'react-router-dom';

const Home = () => {
  return (
    <>
      <header className="d-flex flex-wrap align-items-center justify-content-center justify-content-md-between py-3 mb-4 border-bottom">
      <div className="col-md-3 mb-2 mb-md-0">
        <a href="/" className="d-inline-flex link-body-emphasis text-decoration-none">
          {/* SVG 아이콘은 실제 프로젝트에 맞게 교체 필요 */}
          <svg className="bi" width="40" height="32" role="img" aria-label="Bootstrap">
            <use xlinkHref="#bootstrap"></use>
          </svg>
        </a>
      </div>
      <ul className="nav col-12 col-md-auto mb-2 justify-content-center mb-md-0">
        <li><Link to="/" className="nav-link px-2 link-secondary">Home</Link></li>
        <li><Link to="/Book" className="nav-link px-2">게시글</Link></li>
        <li><a href="projectList.html" className="nav-link px-2">프로젝트</a></li>
        <li><a href="bookstudyList.html" className="nav-link px-2">책공부</a></li>
        <li><a href="compiler.html" className="nav-link px-2">컴파일러</a></li>
        <li><a href="#" className="nav-link px-2">About</a></li>
      </ul>
      <div className="col-md-3 text-end">
        <button type="button" className="btn btn-outline-primary me-2">Login</button>
        <button type="button" className="btn btn-primary">Sign-up</button>
      </div>
    </header>
  </>
  )
}

export default Home

