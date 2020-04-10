package ru.ifmo.web.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.ifmo.web.database.dao.UserDAO;
import ru.ifmo.web.database.dto.UserDTO;
import ru.ifmo.web.database.entity.User;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.sql.DataSource;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Data
@Slf4j
@Path("/users")
@ManagedBean
public class UsersService {

    @Resource(mappedName = "jdbc/users")
    private DataSource dataSource;

    private UserDAO userDAO;

    @PostConstruct
    public void init() {
    userDAO = new  UserDAO(dataSource);
    }
    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @GET
    @Path("/all")
    @Produces({MediaType.APPLICATION_JSON})
    public List<User> findAll() throws SQLException {
        return userDAO.findAll();
    }

    @GET
    @Path("/filter")
    @Produces({MediaType.APPLICATION_JSON})
    public List<User> findWithFilters(@QueryParam("id") Long id, @QueryParam("login") String login,
                                      @QueryParam("password") String password, @QueryParam("email") String email,
                                      @QueryParam("gender") Boolean gender, @QueryParam("registerDate") String registerDate)
            throws SQLException {
        Date date;
        if (registerDate != null) {
            try {
                date = sdf.parse(registerDate);
            } catch (ParseException e) {
                date = null;
            }
        } else {
            date =null;
        }
        return userDAO.findWithFilters(id, login, password, email, gender, date);
    }

    @DELETE
    @Path("/{id}")
    @WebMethod
    @Produces(MediaType.TEXT_PLAIN)
    public String delete(@PathParam("id") Long id)  {
        try {
            if (id == null ) {
                return  "Id can't be null";
            }
            int delete = userDAO.delete(id);
            if (delete <= 0) {
                return String.format("Can't delete User. User with specified id: %s not found ", id);
            }
            return String.valueOf(delete);
        } catch (SQLException e) {
            return  "SQL exception: " + e.getMessage() + ". State: " + e.getSQLState();
        }
    }

    @POST
    @WebMethod
    @Produces(MediaType.TEXT_PLAIN)
    public String insert(UserDTO userDTO)  {
        try {
            Date parse = new SimpleDateFormat("yyyy-MM-dd").parse(userDTO.getRegisterDate());
            return String.valueOf(userDAO.insert(userDTO.getLogin(), userDTO.getPassword(),
                    userDTO.getEmail(), userDTO.getGender(), parse)
            );
        } catch (SQLException e) {
            String message = "SQL exception: " + e.getMessage() + ". State: " + e.getSQLState();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return String.valueOf(-1L);
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String update(@PathParam("id") Long id, UserDTO userDTO)   {
        int update = 0;
        try {
            Date parse = new SimpleDateFormat("yyyy-MM-dd").parse(userDTO.getRegisterDate());
            update = userDAO.update(id, userDTO.getLogin(), userDTO.getPassword(),
                    userDTO.getEmail(), userDTO.getGender(), parse);
            if (update <= 0) {
                return String.format("Can't update User. User with specified id: %s not found ", id);
            }
        } catch (SQLException e) {
            return "SQL exception: " + e.getMessage() + ". State: " + e.getSQLState();
        }  catch (ParseException e) {
            e.printStackTrace();
        }
        return String.valueOf(update);
    }


}
