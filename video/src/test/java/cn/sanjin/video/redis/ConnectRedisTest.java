package cn.sanjin.video.redis;

import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.xml.transform.Templates;

@SpringBootTest
public class ConnectRedisTest {
    public static void main(String[] args) {
        JedisConnectionFactory jcf = new JedisConnectionFactory();
        jcf.afterPropertiesSet();
        var rt = new RedisTemplate<String, String>();
        rt.setConnectionFactory(jcf);
        rt.setDefaultSerializer(StringRedisSerializer.UTF_8);
        rt.afterPropertiesSet();

        rt.opsForValue().set("foo", "bar");
        System.out.println(rt.opsForValue().get("foo"));

        jcf.destroy();

    }
}
