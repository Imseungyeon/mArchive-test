<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<sec:authorize access="isAuthenticated()">
    <sec:authentication property="principal" var="principal"/>
</sec:authorize>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>mArchive</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="/css/styles.css">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.15.4/css/all.css">
</head>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-bs4.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-bs4.min.js"></script>
<body>

<nav class="navbar navbar-expand-md navbar-dark bg-primary">
    <a class="navbar-brand" href="/">mArchive Home</a>
    <button class="navbar-toggler" type="button" date-toggle="collapse" data-target="#collapsibleNavbar">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="collapsibleNavbar">
        <ul class="navbar-nav ml-auto">
            <c:choose>
                <c:when test="${empty principal}">
                    <li class="nav-item">
                        <a class="nav-link" href="/auth/loginForm">Log In</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/auth/joinForm">Sign Up</a>
                    </li>
            </c:when>
            <c:otherwise>
                <li class="navbar-item">
                    <a class="nav-link" href="/board/userPosts">mArchive</a>
                </li>
                <li class="navbar-item">
                    <a class="nav-link" href="/board/saveForm">Write</a>
                </li>
                <li class="navbar-item">
                    <a class="nav-link" href="/auth/updateForm">Info</a>
                </li>
                <li class="navbar-item">
                    <a class="nav-link" href="/logout">Logout</a>
                </li>
            </c:otherwise>
        </c:choose>
        </ul>
    </div>
</nav>
<div class="container mt-4">
<br/>