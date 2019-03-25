package com.blg.dorm.common;

import com.blg.dorm.common.CommonLog;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * redis缓存客户端对象
 */
public class RedisClient {

    private JedisPool jedisPool;
    private int db;
    private boolean useSwitch;

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public int getDb() {
        return db;
    }

    public void setDb(int db) {
        this.db = db;
    }

    public boolean isUseSwitch() {
        return useSwitch;
    }

    public void setUseSwitch(boolean useSwitch) {
        this.useSwitch = useSwitch;
    }

    /**
     * 获取Redis实例.
     *
     * @return Redis工具类实例
     */
    private Jedis openJedis() {
//    	if(!useSwitch){
//    		return null;
//    	}
        Jedis jedis = jedisPool.getResource();
        return jedis;
    }

    /**
     * 释放redis实例到连接池.
     *
     * @param jedis redis实例
     */
    private void closeJedis(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
            //jedisPool.returnResource(jedis);
        }
    }

    /**
     * 根据key获取对象
     *
     * @param key key
     **/
    public Object getObject(String key) {
        return this.unserialize(getObject(key.getBytes()));
    }

    /**
     * 根据key field获取对象
     *
     * @param key   key
     * @param field value
     **/
    public Object hgetObject(String key, String field) {
        return this.unserialize(hgetObject(key.getBytes(), field.getBytes()));
    }

    /**
     * 根据key获取缓存缓存中的二进制
     *
     * @param key key
     ***/
    public Map<String, Object> hgetAllObject(String key) {
        Jedis jd = null;
        Map<String, Object> obj = new HashMap<String, Object>();
        ;
        try {
            jd = this.openJedis();
            if (jd != null) {
                jd.select(this.db);
                Map<byte[], byte[]> cache = jd.hgetAll(key.getBytes());
                if (cache != null) {
                    for (Entry<byte[], byte[]> bean : cache.entrySet()) {
                        obj.put(new String(bean.getKey()), this.unserialize(bean.getValue()));
                    }
                }
            }
        } catch (Exception e) {
            CommonLog.error(CommonLog.getException(e));
        } finally {
            // 关闭连接
            this.closeJedis(jd);
        }
        return obj;
    }

    /***
     * 向缓存中添加对象
     * @param key key
     * @param obj value
     * @throws Exception
     * **/
    public void setObject(String key, Object obj) throws Exception {
        setObjectByKey(key.getBytes(), this.serialize(obj));
    }

    /**
     * 向缓存中添加对象
     *
     * @param key     key
     * @param obj     value
     * @param seconds 单位：秒
     */
    public void setTimeObject(String key, Object obj, int seconds) {
        setTimeObjectByKey(key.getBytes(), this.serialize(obj), seconds);
    }

    /**
     * 向缓存中添加对象
     *
     * @param key   key
     * @param field value
     * @param obj   对象
     */
    public void hsetObject(String key, String field, Object obj) {
        hsetObjectByKey(key.getBytes(), field.getBytes(), this.serialize(obj));
    }

    /**
     * 向缓存中添加对象
     *
     * @param key     key
     * @param field   field
     * @param obj     obj
     * @param seconds 单位：秒
     * @throws Exception
     */
    public void hsetObject(String key, String field, Object obj, int seconds) throws Exception {
        hsetObjectByKey(key.getBytes(), field.getBytes(), this.serialize(obj), seconds);
    }

    /**
     * 根据key获取缓存缓存中的二进制
     *
     * @param key key
     **/
    public byte[] getObject(byte[] key) {
        Jedis jd = null;
        byte[] obj = null;
        try {
            jd = this.openJedis();
            if (jd != null) {
                jd.select(this.db);
                obj = jd.get(key);
            }
        } catch (Exception e) {
            CommonLog.error(CommonLog.getException(e));
        } finally {
            if (jd != null) {
                // 关闭连接
                this.closeJedis(jd);
            }
        }
        return obj;
    }

    /**
     * 根据key获取缓存缓存中的二进制
     *
     * @param key   key
     * @param field field
     ***/
    public byte[] hgetObject(byte[] key, byte[] field) {
        Jedis jd = null;
        byte[] obj = null;
        try {
            jd = this.openJedis();
            if (jd != null) {
                jd.select(this.db);
                obj = jd.hget(key, field);
            }
        } catch (Exception e) {
            CommonLog.error(CommonLog.getException(e));
        } finally {
            if (jd != null) {
                // 关闭连接
                this.closeJedis(jd);
            }
        }
        return obj;
    }

    /**
     * 根据key值设置byte[]
     *
     * @param key    key
     * @param object value
     * @throws Exception
     **/
    public synchronized void setObjectByKey(byte[] key, byte[] object) throws Exception {
        Jedis jd = null;
        try {
            // 获得jedis连接
            jd = this.openJedis();
            if (jd != null) {
                jd.select(this.db);
                jd.set(key, object);
            }

        } catch (Exception e) {
            throw e;
        } finally {
            if (jd != null) {
                // 关闭连接
                this.closeJedis(jd);
            }
        }
    }

    public synchronized void setTimeObjectByKey(byte[] key, byte[] value, int seconds) {
        Jedis jd = null;
        try {
            // 获得jedis连接
            jd = this.openJedis();
            if (jd != null) {
                jd.select(this.db);
                jd.setex(key, seconds, value);
            }

        } catch (Exception e) {
            CommonLog.error(CommonLog.getException(e));
        } finally {
            if (jd != null) {
                // 关闭连接
                this.closeJedis(jd);
            }
        }
    }

    /***
     * 根据key删除缓存
     * @param key key
     */
    public synchronized long delObjectByKey(String key) {
        Jedis jd = null;
        long dels = 0;
        try {
            // 获得jedis连接
            jd = this.openJedis();
            if (jd != null) {
                jd.select(this.db);
                dels = jd.del(key.getBytes());
            }

        } catch (Exception e) {
            CommonLog.error(CommonLog.getException(e));
        } finally {
            if (jd != null) {
                // 关闭连接
                this.closeJedis(jd);
            }
        }
        return dels;
    }

    /**
     * 根据key值设置byte[]
     *
     * @param key    key
     * @param field  field
     * @param object value
     **/
    public synchronized void hsetObjectByKey(byte[] key, byte[] field, byte[] object) {
        Jedis jd = null;
        try {
            // 获得jedis连接
            jd = this.openJedis();
            if (jd != null) {
                jd.select(this.db);
                jd.hset(key, field, object);
            }

        } catch (Exception e) {
            CommonLog.error(CommonLog.getException(e));
        } finally {
            if (jd != null) {
                // 关闭连接
                this.closeJedis(jd);
            }
        }
    }

    /**
     * 根据key值设置byte[]
     *
     * @param key     key
     * @param field   field
     * @param object  object
     * @param seconds seconds
     * @throws Exception
     ***/

    public synchronized void hsetObjectByKey(byte[] key, byte[] field, byte[] object, int seconds) throws Exception {
        Jedis jd = null;
        try {
            // 获得jedis连接
            jd = this.openJedis();
            if (jd != null) {
                jd.select(this.db);
                jd.hset(key, field, object);
                jd.expire(key, seconds);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (jd != null) {
                // 关闭连接
                this.closeJedis(jd);
            }
        }
    }

    private byte[] serialize(Object object) {
        if (object == null) {
            return null;
        }
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        byte[] bytes = null;
        try {
            //序列化
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                    bytes = baos.toByteArray();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bytes;
    }

    private Object unserialize(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        ObjectInputStream ois = null;
        Object obj = null;
        try {
            //反序列化
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bais);
            obj = ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return obj;
    }
}
