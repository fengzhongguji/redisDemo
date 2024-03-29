package com.xql.redis;

import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.*;

public class TestRedis {
    private Jedis jedis;
    @Before
    public void setup(){
         //连接redis服务器
        jedis = new Jedis("192.168.70.133",6379);
        //权限认证，一般不会设置权限
       // jedis.auth("adim");
    }
    @Test
    public  void testString(){
        /*添加数据*/
        jedis.set("name","xinwang");//key为name，value为xinwang
        System.out.println(jedis.get("name"));//执行结果
        //拼接
        jedis.append("name","is my name");
        System.out.println(jedis.get("name"));

        //删除某个键
        jedis.del("name");
        System.out.println(jedis.get("name"));

        //设置多个键值对
        jedis.mset("name","liulian","age","24","qq","556846812");
        //进行+1操作
        jedis.incr("age");
        System.out.println(jedis.get("name") + "-" + jedis.get("age") + "-" + jedis.get("qq"));
    }
    /*redis操作map
    */
    @Test
    public void testMap(){
        Map<String,String> map = new HashMap<String, String>();
        map.put("name","xiwang");
        map.put("age","22");
        map.put("qq","5555555555");
        jedis.hmset("user",map);
        //取出user中的name，执行结果:[minxr]-->注意结果是一个泛型的List
        //第一个参数是存入redis中map对象的key，后面跟的是放入map中的对象的key，后面的key可以跟多个，是可变参数
        List<String> list = jedis.hmget("user", "name", "age", "qq");
        System.out.println(list);
        //删除map中的某个键值
        jedis.hdel("user","age");
        System.out.println(jedis.hmget("user","age"));//因删除所以返回为null
        System.out.println(jedis.hlen("user"));//返回key为user的键中存放的值的个数为2
        System.out.println(jedis.exists("user"));//是否存在key为user的记录返回为true
        System.out.println(jedis.hkeys("user"));//返回map对象的所有key
        System.out.println(jedis.hvals("user"));//返回map对象中的所有value
        Iterator<String> iterator = jedis.hkeys("user").iterator();
        while (iterator.hasNext()){
            String next = iterator.next();
            System.out.println(next+":"+jedis.hmget("user",next));
        }
    }
    /**
     * jedis操作List
     */
    @Test
    public void testList(){
        //开始前，先移除所有的内容
        jedis.del("java framework");
        System.out.println(jedis.lrange("java framework",0,-1));
        //先向key java framework中存放三条数据
        jedis.lpush("java framework","spring");
        jedis.lpush("java framework","struts");
        jedis.lpush("java framework","hibernate");
        //再取出所有数据jedis.lrange是按范围取出，
        // 第一个是key，第二个是起始位置，第三个是结束位置，jedis.llen获取长度 -1表示取得所有
        System.out.println(jedis.lrange("java framework",0,-1));

        jedis.del("java framework");
        jedis.rpush("java framework","spring");
        jedis.rpush("java framework","struts");
        jedis.rpush("java framework","hibernate");
        System.out.println(jedis.lrange("java framework",0,-1));
    }
    /**
     * jedis操作Set
     */
    @Test
    public void testSet(){
        //添加

        jedis.sadd("user1","liulings");
        jedis.sadd("user1","xinxin");
        jedis.sadd("user1","ling");
        jedis.sadd("user1","zhangxinxin");
        jedis.sadd("user1","who");
        //移除noname
        jedis.srem("user1","who");
        System.out.println(jedis.smembers("user1"));//获取所有加入的value
        System.out.println(jedis.sismember("user1", "who"));//判断 who 是否是user集合的元素
        System.out.println(jedis.srandmember("user1"));
        System.out.println(jedis.scard("user1"));//返回集合的元素个数
    }
    @Test
    public void test() throws InterruptedException {
        //jedis 排序
        //注意，此处的rpush和lpush是List的操作。是一个双向链表（但从表现来看的）
        jedis.del("a");//先清除数据，再加入数据进行测试
        jedis.rpush("a", "1");
        jedis.lpush("a","6");
        jedis.lpush("a","3");
        jedis.lpush("a","9");
        System.out.println(jedis.lrange("a",0,-1));// [9, 3, 6, 1]
        System.out.println(jedis.sort("a")); //[1, 3, 6, 9]  //输入排序后结果
        System.out.println(jedis.lrange("a",0,-1));
    }


}
