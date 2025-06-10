// src/components/Layout.jsx
import React from 'react';
import Header from './Header';
import { Outlet } from 'react-router-dom';
import styled from 'styled-components';


const MyMain = styled.main`
  width: 70%;
  margin: 50px auto;
  background-color: #ffffff;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  text-align: center;
`

const Layout = () => (
  <div>
    <Header />
    <MyMain>
      <Outlet />
    </MyMain>
  </div>
);

export default Layout;
