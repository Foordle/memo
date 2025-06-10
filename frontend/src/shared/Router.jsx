import React from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import Home from "../pages/Home";
import BookStudyList from "../pages/BookStudyList";
import Layout from "../components/Layout";
import BookStudyDetail from "../pages/BookStudyDetail.jsx";
import BookStudyWrite from "../pages/BookStudyWrite.jsx";


const Router = () => {
  return (
    <BrowserRouter>  
      <Routes>
        <Route path="/" element = {<Layout/>}>
            <Route index element={<Home/>}/>
            <Route path="/BookStudyList" element={<BookStudyList/>}/>
            <Route path="/BookStudyDetail" element={<BookStudyDetail/>}/>
            <Route path="/BookStudyWrite" element={<BookStudyWrite/>}/>

        </Route>
      </Routes>
    </BrowserRouter>
  );
};

export default Router;