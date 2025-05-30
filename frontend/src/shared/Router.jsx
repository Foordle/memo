import React from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import Home from "../pages/Home";
import Book from "../pages/book";
import Layout from "../components/Layout";


const Router = () => {
  return (
    <BrowserRouter>  
      <Routes>
        <Route path="/" element = {<Layout/>}>
            <Route index element={<Home/>}/>
            <Route path="/book" element={<Book/>}/>
        </Route>
      </Routes>
    </BrowserRouter>
  );
};

export default Router;