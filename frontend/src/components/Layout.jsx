// src/components/Layout.jsx
import React from 'react';
import Header from './Header';
import { Outlet } from 'react-router-dom';

const Layout = () => (
  <div>
    <Header />
    <main>
      <Outlet />
    </main>
  </div>
);

export default Layout;
