package com.newportal.demo.mapper;

import com.newportal.demo.dto.RoleDTO;
import com.newportal.demo.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleDTOMapper {
    RoleDTO roleToRoleDTO(Role role);
    Role roleDTOToRole(RoleDTO roleDTO);

}
