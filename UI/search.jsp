<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<!--
  Material Design Lite
  Copyright 2015 Google Inc. All rights reserved.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      https://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License
-->

<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="description" content="Introducing Lollipop, a sweet new take on Android.">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0">
    <title>Project 4 - #demonetization </title>

    <!-- Page styles -->
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto:regular,bold,italic,thin,light,bolditalic,black,medium&amp;lang=en">
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="https://code.getmdl.io/1.2.1/material.min.css">
    <link rel="stylesheet" href="styles.css">
    
    <style>
    #view-source {
      position: fixed;
      display: block;
      right: 0;
      bottom: 0;
      margin-right: 40px;
      margin-bottom: 40px;
      z-index: 900;
    }
    </style>
  </head>
  <body>
    <div class="mdl-layout mdl-js-layout mdl-layout--fixed-header" style="background-color:#fafafa">

      <div class="android-header mdl-layout__header mdl-layout__header--waterfall">
        <div class="mdl-layout__header-row" style="max-width: 1044px;margin-left: auto;margin-right: auto;height: 100px;padding:0px">
          <span class="android-title mdl-layout-title">           
            <span class="mdl-typography--text-uppercase " style="color:green;fonr:20px">Demonetization </span>
            <form class="form-wrapper" style="display:inline" action="Servlet" method="POST">
              <input type="text" class="" name="search" id="search" style="height:35px;width:250px; padding-left: 10px; margin-left: 5px;" placeholder="Type your question" >
               <button type="submit" value="go" id="submit" style="height:35px">
                <label class="mdl-button mdl-js-button mdl-button--icon" for="search-field">
                  <i type="submit" id="submit" class="material-icons">search</i>
                </label>
               </button>
           </form>
             
          </span>
          
          <!-- Add spacer, to align navigation to the right in desktop -->
          <div class="android-header-spacer mdl-layout-spacer"></div>
          
          <!-- Navigation -->
          <div class="android-navigation-container">
            <nav class="android-navigation mdl-navigation">
              <a class="mdl-navigation__link mdl-typography--text-uppercase" href="#">Where</a>
              <a class="mdl-navigation__link mdl-typography--text-uppercase" href="#">When</a>
              <a class="mdl-navigation__link mdl-typography--text-uppercase" href="#">Who</a>
              </nav>
          </div>          
        </div>
      </div>

      

      <div class="android-content mdl-layout__content">
                
                      
        <div class="android-more-section">
          <div class="android-card-container mdl-grid">
           <c:forEach items="${output}" var="singleoutput">
                
                <div class="mdl-cell mdl-cell--3-col mdl-cell--4-col-tablet mdl-cell--4-col-phone mdl-card mdl-shadow--3dp">
                  

                  <div class="mdl-card__supporting-text">
                    <span class="mdl-typography--font-light mdl-typography--subhead">${singleoutput}</span>
                  </div>
                </div>
            </c:forEach>     
           </div>
        </div>

      </div>
    </div>
    
    <script src="https://code.getmdl.io/1.2.1/material.min.js"></script>
  </body>
</html>
