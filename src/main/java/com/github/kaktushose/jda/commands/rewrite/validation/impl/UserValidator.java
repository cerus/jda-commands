package com.github.kaktushose.jda.commands.rewrite.validation.impl;

import com.github.kaktushose.jda.commands.rewrite.adapters.impl.MemberAdapter;
import com.github.kaktushose.jda.commands.rewrite.annotations.constraints.User;
import com.github.kaktushose.jda.commands.rewrite.dispatching.CommandContext;
import com.github.kaktushose.jda.commands.rewrite.validation.Validator;
import net.dv8tion.jda.api.entities.Member;

import java.util.Optional;

public class UserValidator implements Validator {

    @Override
    public boolean validate(Object argument, Object annotation, CommandContext context) {
        Member member = (Member) argument;
        User user = (User) annotation;
        Optional<Member> optional = new MemberAdapter().parse(user.value(), context);
        return optional.filter(member::equals).isPresent();
    }
}
