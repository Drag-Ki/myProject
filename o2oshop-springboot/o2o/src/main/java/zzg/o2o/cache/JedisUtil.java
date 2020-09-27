package zzg.o2o.cache;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.SortingParams;
import redis.clients.util.SafeEncoder;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class JedisUtil {
    private final int expire=60000;
    public Keys KEYS;
    public Strings STRINGS;
    public Lists LISTS;
    public Sets SETS;
    public Hash HASH;

    private JedisPool jedisPool;

    public JedisPool getJedisPool(){
        return jedisPool;
    }

    public void setJedisPool(JedisPoolWriper jedisPoolWriper){
        this.jedisPool=jedisPoolWriper.getJedisPool();
    }

    public Jedis getJedis(){
        return jedisPool.getResource();
    }

    public void expire(String key,int seconds){
        if(seconds<=0){
            return;
        }
        Jedis jedis=getJedis();
        jedis.expire(key,seconds);
        jedis.close();
    }

    public void expire(String key){
        expire(key,expire);
    }

    public class Keys{
        public Keys(){}

        public String flushAll(){
            Jedis jedis=getJedis();
            String stata=jedis.flushAll();
            jedis.close();
            return stata;
        }

        public String rename(String oldkey,String newkey){
            return rename(SafeEncoder.encode(oldkey), SafeEncoder.encode(newkey));
        }

        public long renamenx(String oldkey,String newkey){
            Jedis jedis=getJedis();
            long status=jedis.renamenx(oldkey,newkey);
            jedis.close();
            return status;
        }

        public String rename(byte[] oldkey,byte[] newkey){
            Jedis jedis=getJedis();
            String status=jedis.rename(oldkey,newkey);
            jedis.close();
            return status;
        }

        public long expired(String key,int seconds){
            Jedis jedis=getJedis();
            long count=jedis.expire(key,seconds);
            jedis.close();
            return count;
        }

        public long expireAt(String key,long timestamp){
            Jedis jedis=getJedis();
            long count=jedis.expireAt(key,timestamp);
            jedis.close();
            return count;
        }

        public long ttl(String key){
            Jedis sjedis=getJedis();
            long len=sjedis.ttl(key);
            sjedis.close();
            return len;
        }

        public long persist(String key){
            Jedis jedis=getJedis();
            long count=jedis.persist(key);
            jedis.close();
            return count;
        }

        public long del(String... keys){
            Jedis jedis=getJedis();
            long count=jedis.del(keys);
            jedis.close();
            return count;
        }

        public long del(byte[]... keys){
            Jedis jedis=getJedis();
            long count=jedis.del(keys);
            jedis.close();
            return count;
        }

        public boolean exists(String key){
            Jedis sjedis=getJedis();
            boolean exis=sjedis.exists(key);
            sjedis.close();
            return exis;
        }

        public List<String> sort(String key){
            Jedis sjedis=getJedis();
            List<String> list=sjedis.sort(key);
            sjedis.close();
            return list;
        }

        public List<String> sort(String key, SortingParams params){
            Jedis sjedis=getJedis();
            List<String> list=sjedis.sort(key,params);
            sjedis.close();
            return list;
        }

        public String type(String key){
            Jedis sjedis=getJedis();
            String type=sjedis.type(key);
            sjedis.close();
            return type;
        }

        public Set<String> keys(String pattern){
            Jedis jedis=getJedis();
            Set<String> set=jedis.keys(pattern);
            jedis.close();
            return set;
        }
    }

    public class Strings{
        public String get(String key){
            Jedis sjedis=getJedis();
            String value=sjedis.get(key);
            sjedis.close();
            return value;
        }

        public byte[] get(byte[] key){
            Jedis sjedis=getJedis();
            byte[] value=sjedis.get(key);
            sjedis.close();
            return value;
        }

        public String set(String key,String value){
            return set(SafeEncoder.encode(key),SafeEncoder.encode(value));
        }

        public String set(String key,byte[] value){
            return set(SafeEncoder.encode(key),value);
        }

        public String set(byte[] key,byte[] value){
            Jedis jedis=getJedis();
            String status=jedis.set(key,value);
            jedis.close();
            return status;
        }

        public String setEx(String key,int seconds,String value){
            Jedis jedis=getJedis();
            String str=jedis.setex(key,seconds,value);
            jedis.close();
            return str;
        }

        public String setEx(byte[] key,int seconds,byte[] value){
            Jedis jedis=getJedis();
            String str=jedis.setex(key,seconds,value);
            jedis.close();
            return str;
        }

        public long setnx(String key,String value){
            Jedis jedis=getJedis();
            long str=jedis.setnx(key,value);
            jedis.close();
            return str;
        }

        public long setRange(String key,long offset,String value){
            Jedis jedis=getJedis();
            long len=jedis.setrange(key,offset,value);
            jedis.close();
            return len;
        }

        public long append(String key,String value){
            Jedis jedis=getJedis();
            long len=jedis.append(key,value);
            jedis.close();
            return len;
        }

        public long decrBy(String key,long number){
            Jedis jedis=getJedis();
            long len=jedis.decrBy(key,number);
            jedis.close();
            return len;
        }

        public long incrBy(String key,long number){
            Jedis jedis=getJedis();
            long len=jedis.incrBy(key,number);
            jedis.close();
            return len;
        }

        public String getrange(String key,long startOffset,long endOffset){
            Jedis sjedis=getJedis();
            String value=sjedis.getrange(key,startOffset,endOffset);
            sjedis.close();
            return value;
        }

        public String getSet(String key,String value){
            Jedis jedis=getJedis();
            String str=jedis.getSet(key,value);
            jedis.close();
            return str;
        }

        public List<String> mget(String... keys){
            Jedis jedis=getJedis();
            List<String> str=jedis.mget(keys);
            jedis.close();
            return str;
        }

        public String mset(String ... keysvalues){
            Jedis jedis=getJedis();
            String str=jedis.mset(keysvalues);
            jedis.close();
            return str;
        }

        public long strlen(String key){
            Jedis jedis=getJedis();
            long len=jedis.strlen(key);
            jedis.close();
            return len;
        }
    }
    public class Sets{
        public long sadd(String key,String member){
            Jedis jedis=getJedis();
            long s=jedis.sadd(key,member);
            jedis.close();
            return s;
        }

        public long sadd(byte[] key,byte[] member){
            Jedis jedis=getJedis();
            long s=jedis.sadd(key,member);
            jedis.close();
            return s;
        }

        public long scard(String key){
            Jedis sjedis=getJedis();
            long len=sjedis.scard(key);
            sjedis.close();
            return len;
        }

        public Set<String> sdiff(String... keys){
            Jedis jedis=getJedis();
            Set<String> set=jedis.sdiff(keys);
            jedis.close();
            return set;
        }

        public long sdiffstore(String newkey,String... keys){
            Jedis jedis=getJedis();
            long s=jedis.sdiffstore(newkey,keys);
            jedis.close();
            return s;
        }

        public Set<String> sinter(String... keys){
            Jedis jedis=getJedis();
            Set<String> set=jedis.sinter(keys);
            jedis.close();
            return set;
        }

        public long sinterstore(String newkey,String... keys){
            Jedis jedis=getJedis();
            long s=jedis.sinterstore(newkey,keys);
            jedis.close();
            return s;
        }

        public boolean sismember(String key,String member){
            Jedis sjedis=getJedis();
            boolean s=sjedis.sismember(key,member);
            sjedis.close();
            return s;
        }

        public Set<String> smembers(String key){
            Jedis sjedis=getJedis();
            Set<String> set=sjedis.smembers(key);
            sjedis.close();
            return set;
        }

        public Set<byte[]> smembers(byte[] key){
            Jedis sjedis=getJedis();
            Set<byte[]> set=sjedis.smembers(key);
            sjedis.close();
            return set;
        }

        public long smove(String srckey,String dstkey,String member){
            Jedis jedis=getJedis();
            long s=jedis.smove(srckey,dstkey,member);
            jedis.close();
            return s;
        }

        public String spop(String key){
            Jedis jedis=getJedis();
            String s=jedis.spop(key);
            jedis.close();
            return s;
        }

        public long srem(String key,String member){
            Jedis jedis=getJedis();
            long s=jedis.srem(key,member);
            jedis.close();
            return s;
        }

        public Set<String> sunion(String... keys){
            Jedis jedis=getJedis();
            Set<String> set=jedis.sunion(keys);
            jedis.close();
            return set;
        }

        public long sunionstore(String newkey,String... keys){
            Jedis jedis=getJedis();
            long s=jedis.sunionstore(newkey,keys);
            jedis.close();
            return s;
        }
    }

    public class Hash{
        public long hdel(String key,String field){
            Jedis jedis=getJedis();
            long s=jedis.hdel(key,field);
            jedis.close();
            return s;
        }

        public long hdel(String key){
            Jedis jedis=getJedis();
            long s=jedis.del(key);
            jedis.close();
            return s;
        }

        public boolean hexists(String key,String field){
            Jedis sjedis=getJedis();
            boolean s=sjedis.hexists(key,field);
            sjedis.close();
            return s;
        }

        public String hget(String key,String field){
            Jedis sjedis=getJedis();
            String s=sjedis.hget(key,field);
            sjedis.close();
            return s;
        }

        public byte[] hget(byte[] key,byte[] field){
            Jedis sjedis=getJedis();
            byte[] s=sjedis.hget(key,field);
            sjedis.close();
            return s;
        }

        public Map<String,String> hgetAll(String key){
            Jedis sjedis=getJedis();
            Map<String,String> map=sjedis.hgetAll(key);
            sjedis.close();
            return map;
        }

        public long hset(String key,String field,String value){
            Jedis jedis=getJedis();
            long s=jedis.hset(key,field,value);
            jedis.close();
            return s;
        }

        public long hset(String key,String field,byte[] value){
            Jedis jedis=getJedis();
            long s=jedis.hset(key.getBytes(),field.getBytes(),value);
            jedis.close();
            return s;
        }

        public long hsetnx(String key,String field,String value){
            Jedis jedis=getJedis();
            long s=jedis.hsetnx(key,field,value);
            jedis.close();
            return s;
        }

        public List<String> hvals(String key){
            Jedis sjedis=getJedis();
            List<String> list=sjedis.hvals(key);
            sjedis.close();
            return list;
        }

        public long hincrby(String key,String field,long value){
            Jedis jedis=getJedis();
            long s=jedis.hincrBy(key,field,value);
            jedis.close();
            return s;
        }

        public Set<String> hkeys(String key){
            Jedis sjedis=getJedis();
            Set<String> set=sjedis.hkeys(key);
            sjedis.close();
            return set;
        }

        public long hlen(String key){
            Jedis sjedis=getJedis();
            long len=sjedis.hlen(key);
            sjedis.close();
            return len;
        }

        public List<String> hmget(String key,String... fields){
            Jedis sjedis=getJedis();
            List<String> list=sjedis.hmget(key,fields);
            sjedis.close();
            return list;
        }

        public List<byte[]> hmget(byte[] key,byte[]... fields){
            Jedis sjedis=getJedis();
            List<byte[]> list=sjedis.hmget(key,fields);
            sjedis.close();
            return list;
        }

        public String hmset(String key,Map<String,String> map){
            Jedis jedis=getJedis();
            String s=jedis.hmset(key,map);
            jedis.close();
            return s;
        }

        public String hmset(byte[] key,Map<byte[],byte[]> map){
            Jedis jedis=getJedis();
            String s=jedis.hmset(key,map);
            jedis.close();
            return s;
        }
    }

    public class Lists{
        public long llen(String key){
            return llen(SafeEncoder.encode(key));
        }

        public long llen(byte[] key){
            Jedis sjedis=getJedis();
            long count=sjedis.llen(key);
            sjedis.close();
            return count;
        }

        public String lset(byte[] key,int index,byte[] value){
            Jedis jedis=getJedis();
            String status=jedis.lset(key,index,value);
            jedis.close();
            return status;
        }

        public String lset(String key,int index,String value){
            return lset(SafeEncoder.encode(key),index,SafeEncoder.encode(value));
        }

        public long linsert(String key,LIST_POSITION where,String pivot,String value){
            return linsert(SafeEncoder.encode(key),where,SafeEncoder.encode(pivot),SafeEncoder.encode(value));
        }

        public long linsert(byte[] key,LIST_POSITION where,byte[] pivot,byte[] value){
            Jedis jedis=getJedis();
            long count=jedis.linsert(key,where,pivot,value);
            jedis.close();
            return count;
        }

        public String lindex(String key,int index){
            return SafeEncoder.encode(lindex(SafeEncoder.encode(key),index));
        }

        public byte[] lindex(byte[] key,int index){
            Jedis sjedis=getJedis();
            byte[] value=sjedis.lindex(key,index);
            sjedis.close();
            return value;
        }

        public String lpop(String key){
            return SafeEncoder.encode(lpop(SafeEncoder.encode(key)));
        }

        public byte[] lpop(byte[] key){
            Jedis jedis=getJedis();
            byte[] value=jedis.lpop(key);
            jedis.close();
            return value;
        }

        public String rpop(String key){
            Jedis jedis=getJedis();
            String value=jedis.rpop(key);
            jedis.close();
            return value;
        }

        public long lpush(String key,String value){
            return lpush(SafeEncoder.encode(key),SafeEncoder.encode(value));
        }

        public long rpush(String key,String value){
            Jedis jedis=getJedis();
            long count=jedis.rpush(key,value);
            jedis.close();
            return count;
        }

        public long rpush(byte[] key,byte[] value){
            Jedis jedis=getJedis();
            long count=jedis.rpush(key,value);
            jedis.close();
            return count;
        }

        public long lpush(byte[] key,byte[] value){
            Jedis jedis=getJedis();
            long count=jedis.lpush(key,value);
            jedis.close();
            return count;
        }

        public List<String> lrange(String key,long start,long end){
            Jedis sjedis=getJedis();
            List<String> list=sjedis.lrange(key,start,end);
            sjedis.close();
            return list;
        }

        public List<byte[]> lrange(byte[] key,int start,int end){
            Jedis sjedis=getJedis();
            List<byte[]> list=sjedis.lrange(key,start,end);
            sjedis.close();
            return list;
        }

        public long lrem(byte[] key,int c,byte[] value){
            Jedis jedis=getJedis();
            long count=jedis.lrem(key,c,value);
            jedis.close();
            return count;
        }

        public long lrem(String key,int c,String value){
            return lrem(SafeEncoder.encode(key),c,SafeEncoder.encode(value));
        }

        public String ltrim(byte[] key,int start,int end){
            Jedis jedis=getJedis();
            String str=jedis.ltrim(key,start,end);
            jedis.close();
            return str;
        }

        public String ltrim(String key,int start,int end){
            return ltrim(SafeEncoder.encode(key),start,end);
        }
    }

}
