package com.course.cases;

import com.course.config.TestConfig;
import com.course.model.GetUserListCase;
import com.course.model.User;
import com.course.utils.DatabaseUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class GetUserInfoListTest {



    @Test(dependsOnGroups="loginTrue",description = "获取性别为男的用户信息")
    public void getUserListInfo() throws IOException, InterruptedException {

        SqlSession session = DatabaseUtil.getSqlSession();
        GetUserListCase getUserListCase = session.selectOne("getUserListCase",1);
        System.out.println(getUserListCase.toString());
        System.out.println(TestConfig.getUserListUrl);


        //下边为写完接口的代码
        //发送请求获取结果
        /**
         * 可以先讲
         */
        Thread.sleep(2000);
        //验证
        List<User> userList = session.selectList(getUserListCase.getExpected(),getUserListCase);
        for(User u : userList){
            System.out.println("从数据库中获取的user:"+u.toString());
        }//这段话可以不写
        JSONArray userListJson = new JSONArray(userList);//从数据库中拿到的


        JSONArray api_userListJson = getJsonResult(getUserListCase);//从接口中拿到的。
        Assert.assertEquals(userListJson.length(),api_userListJson.length());
        for(int i = 0;i<api_userListJson.length();i++){
            JSONObject expectMysql = (JSONObject) userListJson.get(i);
            JSONObject actualApi= (JSONObject) api_userListJson.get(i);

            Assert.assertEquals( expectMysql.toString(),actualApi.toString());
        }

    }

    private JSONArray getJsonResult(GetUserListCase getUserListCase) throws IOException {
        HttpPost post = new HttpPost(TestConfig.getUserListUrl);
        JSONObject param = new JSONObject();
      //  param.put("userName",getUserListCase.getUserName());
        param.put("sex",getUserListCase.getSex());
        //param.put("age",getUserListCase.getAge());
        //设置请求头信息 设置header
        post.setHeader("content-type","application/json");
        //将参数信息添加到方法中
        StringEntity entity = new StringEntity(param.toString(),"utf-8");
        post.setEntity(entity);
        //设置cookies
        TestConfig.defaultHttpClient.setCookieStore(TestConfig.store);
        //声明一个对象来进行响应结果的存储
        String result;
        //执行post方法
        HttpResponse response = TestConfig.defaultHttpClient.execute(post);
        //获取响应结果
        result = EntityUtils.toString(response.getEntity(),"utf-8");
        JSONArray jsonArray = new JSONArray(result);
        System.out.println("调用接口list result:"+result);
        return jsonArray;//多个用户的信息

    }

}
