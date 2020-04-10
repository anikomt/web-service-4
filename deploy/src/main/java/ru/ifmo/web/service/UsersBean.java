package ru.ifmo.web.service;

import lombok.Data;
import ru.ifmo.web.database.dao.UserDAO;

import javax.annotation.ManagedBean;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.sql.DataSource;

@Data
@ManagedBean
public class UsersBean {
    @Resource(mappedName = "jdbc/users")
    private DataSource dataSource;

    @Produces
    public UserDAO userDAO() {
        if (true) {
            throw new RuntimeException("FUCK");
        }
        return new UserDAO(dataSource);
    }
}
