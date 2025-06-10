import React from 'react'
import { Link } from 'react-router-dom';
import styled from 'styled-components'

const WrapDiv = styled.div`
  width: 500px;
`
const StDiv = styled.main`
    padding-top: 100px;
    display: flex;
    flex-direction: column;
`

const Home = () => {


  return (
    <>
        <StDiv>
        <WrapDiv className="wrap" id="auth-div">
          <form action="/login" method="post">
            <h1 className="h3 mb-3 fw-normal">Please sign in</h1>
            <div className="form-floating">
              <input
                type="text"
                className="form-control"
                id="username"
                placeholder="Your id"
                name="username"
                required=""
              />
              <label htmlFor="username">Your id</label>
            </div>
            <div className="form-floating">
              <input
                type="password"
                className="form-control"
                id="password"
                placeholder="Password"
                name="password"
                required=""
              />
              <label htmlFor="password">Password</label>
            </div>
            <div className="form-check text-start my-3">
              <input
                className="form-check-input"
                type="checkbox"
                defaultValue="remember-me"
                id="checkDefault"
              />
              <label className="form-check-label" htmlFor="checkDefault">
                Remember me
              </label>
            </div>
            <button className="btn btn-primary w-100 py-2" id="loginBtn" type="submit">
              Login
            </button>
            <a href="/register.html" id="registerBtn" style={{ display: "none" }}>
              Sign up
            </a>
            <a href="/logout" id="logoutBtn" style={{ display: "none" }}>
              Logout
            </a>
          </form>
        </WrapDiv>
        </StDiv>

    </>
  )
}

export default Home

