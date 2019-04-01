package com.sopranoworks;

import org.jgroups.Address;
import org.jgroups.annotations.Property;
import org.jgroups.conf.ClassConfigurator;
import org.jgroups.protocols.FILE_PING;
import org.jgroups.protocols.PingData;
import org.jgroups.util.NameCache;
import org.jgroups.util.Responses;
import org.jgroups.util.Util;
import redis.clients.jedis.Jedis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class REDIS_PING extends FILE_PING {
    protected static final short REDIS_PING_ID=3210;

    static {
        ClassConfigurator.addProtocol(REDIS_PING_ID, REDIS_PING.class);
    }

    private Jedis _jedis;

    @Property(description="The redis host address")
    protected String redis_host = "localhost";

    @Property(description="The redis service port")
    protected int redis_port = 6379;

    @Property(description="The redis db number for the cluster")
    protected int redis_db = 0;

    @Override
    public void init() throws Exception {
        super.init();

        if (redis_host == null) throw new Exception("redis_host is not set");
        _jedis = new Jedis(redis_host, redis_port);
        _jedis.connect();
        _jedis.select(redis_db);
    }
    
    protected static String addressToFilename(String clustername,Address mbr) {
        String logical_name= NameCache.get(mbr);
        String name=addressAsString(mbr) + (logical_name != null? logical_name : "");
        return clustername + ":" + name;//regexp.matcher(name).replaceAll("-");
    }

    @Override
    protected void createRootDir() {
    }

    @Override
    protected void remove(String clustername, Address addr) {
        if(clustername == null || addr == null)
            return;

        log.debug("remove %s", clustername);

        String keyname = addressToFilename(clustername,addr);
        try {
            _jedis.del(keyname);
        } catch(Exception e) {
        }
    }

    @Override
    protected void removeAll(String clustername) {
        if(clustername == null)
            return;
        try {
            for (String key : _jedis.keys(clustername + ":*")) {
                try {
                    _jedis.del(key);
                } catch(Exception e) {
                }
            }
        } catch(Exception e) {

        }
    }

    @Override
    protected void readAll(List<Address> members, String clustername, Responses responses) {
        if(clustername == null)
            return;
        try {
            for (String key :_jedis.keys(clustername + ":*")) {
                List<PingData> list=null;
                try {
                    list = read(new ByteArrayInputStream(_jedis.get(key).getBytes()));
                } catch(Exception e) {

                }
                if(list == null) {
                    log.warn("failed reading " + key);
                    continue;
                }
                for(PingData data: list) {
                    if(members == null || members.contains(data.getAddress()))
                        responses.addResponse(data, true);
                    if(local_addr != null && !local_addr.equals(data.getAddress()))
                        addDiscoveryResponseToCaches(data.getAddress(), data.getLogicalName(), data.getPhysicalAddr());
                }
            }
        } catch (Exception e) {

        }
    }

    @Override
    protected void write(List<PingData> list, String clustername) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        String keyname = addressToFilename(clustername,local_addr);

        try {
            write(list, os);
//            _jedis.psetex(keyname,info_writer_sleep_time * 2,new String(os.toByteArray()));
            _jedis.set(keyname,new String(os.toByteArray()));
        }
        catch(Exception ioe) {
            log.error(Util.getMessage("AttemptToWriteDataFailedAt") + clustername, ioe);
        }
    }
}
