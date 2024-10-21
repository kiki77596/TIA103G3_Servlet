package com.member.control;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mindrot.jbcrypt.BCrypt;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.member.model.MemberJDBC;
import com.member.model.MemberVO;

@WebServlet("/MemberRegister")
public class MemberRegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 5673675033351078850L;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 跨域請求設定
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");

        // 處理 OPTIONS 預檢請求
        if (req.getMethod().equalsIgnoreCase("OPTIONS")) {
            resp.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        
        super.service(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 設置請求與響應的編碼格式
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");

        // 讀取 JSON 資料
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"無效的 JSON 格式。\"}");
            return;
        }

        String json = jsonBuilder.toString();

        // 將 JSON 轉換為 MemberVO 物件
        Gson gson = new Gson();
        MemberVO memberVO;
        try {
            memberVO = gson.fromJson(json, MemberVO.class);
        } catch (JsonSyntaxException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"無效的 JSON 格式。\"}");
            return;
        }

        // 檢查資料是否完整
        if (memberVO.getName() == null || memberVO.getEmail() == null || memberVO.getPassword() == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"資料不完整，請檢查必填欄位。\"}");
            return;
        }

        // 檢查 email 是否重複
        MemberJDBC memberJDBC = new MemberJDBC();
        if (memberJDBC.isEmailExists(memberVO.getEmail())) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"該電子郵件已被使用，請更換電子郵件。\"}");
            return;
        }

//      // 使用BCrypt加密密碼
//      String hashedPassword = BCrypt.hashpw(memberVO.getPassword(), BCrypt.gensalt());
//      memberVO.setPassword(hashedPassword);
      String password = memberVO.getPassword();
      memberVO.setPassword(password);

        // 插入會員資料到資料庫
        try {
            memberJDBC.insert(memberVO);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"message\": \"註冊成功！\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"註冊失敗，請稍後再試。\"}");
            e.printStackTrace();
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write("<h1>歡迎使用會員註冊頁面</h1>");
    }

}
