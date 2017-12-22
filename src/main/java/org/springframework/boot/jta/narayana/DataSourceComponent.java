package org.springframework.boot.jta.narayana;

import com.arjuna.ats.jbossatx.jta.RecoveryManagerService;
import org.postgresql.xa.PGXADataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jta.XADataSourceWrapper;
import org.springframework.boot.jta.narayana.DataSourceXAResourceRecoveryHelper;
import org.springframework.boot.jta.narayana.NarayanaDataSourceBean;
import org.springframework.boot.jta.narayana.NarayanaRecoveryManagerBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import javax.sql.XAConnection;
import javax.sql.XADataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

@Component
public class DataSourceComponent {

    //@Autowired
    //private XADataSourceWrapper xaDataSourceWrapper;

    @Autowired
    private NarayanaRecoveryManagerBean recoveryManager;

    @Bean XADataSource xaDataSource(
            @Value("${spring.datasource.url}") String url,
            @Value("${spring.datasource.username}") String user,
            @Value("${spring.datasource.password}") String password) {
        PGXADataSource xa = new PGXADataSource();
        xa.setUser(user);
        xa.setPassword(password);
        xa.setUrl(url);
        return xa;
    }

    @Bean
    public DataSource dataSource(XADataSource xaDataSource,
                                 @Value("${spring.datasource.username}") String user,
                                 @Value("${spring.datasource.password}") String password) throws Exception {
        org.apache.tomcat.jdbc.pool.XADataSource ds = new org.apache.tomcat.jdbc.pool.XADataSource();
        ds.setPassword(password);
        ds.setUsername(user);
        ds.setDataSource(xaDataSource);

        // Regist XARecovery
        DataSourceXAResourceRecoveryHelper helper = new DataSourceXAResourceRecoveryHelper(ds);
        recoveryManager.registerXAResourceRecoveryHelper(helper);



        //NarayanaDataSourceBean ds = (NarayanaDataSourceBean) xaDataSourceWrapper.wrapDataSource(xaDataSource);


        return ds;
    }
}
