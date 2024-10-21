package com.member.control;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.member.model.MemberJDBC;
import com.member.model.MemberVO;

@WebServlet("/getOneMember")
public class GetOneMemberServlet extends HttpServlet {

    private static final long serialVersionUID = -5337326634130632679L;

    // 假设 dataSource 已初始化
    private MemberJDBC memberJDBC = new MemberJDBC(/* your DataSource instance here */);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 設定 CORS Headers
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        // 設定回應格式
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        // 取得請求中的 email 參數
        String email = req.getParameter("email");
        if (email == null || email.isEmpty()) {
            resp.getWriter().write("{\"error\":\"Email parameter is missing\"}");
            return;
        }

        // 查詢會員資料
        MemberVO mm = memberJDBC.findByEmail(email);

        // 如果找不到會員，回傳錯誤訊息
        if (mm == null) {
            resp.getWriter().write("{\"error\":\"Member not found\"}");
            return;
        }

        // 使用 GsonBuilder 來處理日期格式
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd") // 指定日期格式
                .create();

        // 將會員資料轉換為 JSON 並發送
        String jsonString = gson.toJson(mm);
        resp.getWriter().write(jsonString);
        System.out.println("資料已成功發送到前端囉~~");
    }
}
