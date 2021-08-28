package com.github.kaktushose.jda.commands.rewrite.validation.impl;

import com.github.kaktushose.jda.commands.rewrite.adapters.impl.RoleAdapter;
import com.github.kaktushose.jda.commands.rewrite.validation.Validator;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.util.Optional;

public class NotRoleValidator implements Validator {
    @Override
    public boolean validate(Object argument, Object annotation, Guild guild) {
        com.github.kaktushose.jda.commands.rewrite.annotations.constraints.Role roleAnnotation = (com.github.kaktushose.jda.commands.rewrite.annotations.constraints.Role) annotation;

        Optional<Role> optional = new RoleAdapter().parse(roleAnnotation.value(), guild);
        Member member = (Member) argument;

        return !optional.filter(role -> member.getRoles().contains(role)).isPresent();
    }
}
