package com.comicsopen.web.controller;
import org.springframework.security.web.csrf.CsrfToken;import org.springframework.web.bind.annotation.*;
@RestController public class CsrfController {@GetMapping("/api/csrf") public String csrf(CsrfToken token){return token.getToken();}}
