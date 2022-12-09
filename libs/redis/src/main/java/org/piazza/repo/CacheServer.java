package org.piazza.repo;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.Map;

@Component
public class CacheServer {
    @Autowired
    public JedisPool pool;
    private Jedis jedis;


    @Autowired
    public CacheServer(JedisPool pool) {
        this.pool = pool;
    }

    public JSONObject find(String collectionName, String key) throws JSONException {
        return new JSONObject(jedis.hget(collectionName, key));
    }

    public ArrayList<JSONObject> findAll(String collectionName) throws JSONException {
        Map<String, String> object = jedis.hgetAll(collectionName);
        ArrayList<JSONObject> jsons = new ArrayList<JSONObject>();
        for (String key : object.keySet()) {
            jsons.add(new JSONObject((String) object.get(key)));
        }
        return jsons;

    }

    public Jedis connect() {
        return pool.getResource();
    }

    public void delete(String collectionName, String key) {
        Jedis jedis = connect();
        jedis.del(key);
        pool.returnResource(jedis);
//        jedis.close();
    }


    public void save(String collectionName, String key, String data) {
        Jedis jedis = connect();
        jedis.set(key, collectionName);
        pool.returnResource(jedis);
//        jedis.close();
    }


    public boolean existsInCache(String collectionName, String id) {
        Jedis jedis = connect();
        boolean t = jedis.exists(id);
        pool.returnResource(jedis);
//        jedis.close();
        return t;
    }


    public void dropCollection(String collectionName) {
        jedis.del(collectionName);
    }


    public void closeConnection() {
        jedis.close();
    }
    public int getNumberOfConnections() {
       return pool.getNumActive();
    }

}
