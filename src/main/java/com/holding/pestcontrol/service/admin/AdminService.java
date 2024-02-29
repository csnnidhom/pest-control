package com.holding.pestcontrol.service.admin;

import com.holding.pestcontrol.dto.ReqResAdminCreateUpdate;
import com.holding.pestcontrol.dto.ReqResAdminGetDelete;
import com.holding.pestcontrol.entity.Role;
import com.holding.pestcontrol.entity.User;

import java.util.List;

public interface AdminService {

    ReqResAdminCreateUpdate create(ReqResAdminCreateUpdate reqResAdminCreateUpdate);

    ReqResAdminCreateUpdate update(ReqResAdminCreateUpdate reqResAdminCreateUpdate);

    ReqResAdminGetDelete getDetailByEmail(ReqResAdminGetDelete reqResAdminGetDelete);

    List<User> getAllDataByRole(Role role);

    ReqResAdminGetDelete switchStatus(ReqResAdminGetDelete reqResAdminGetDelete);

}
