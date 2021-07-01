package com.company.betternav.util.validators;

import be.dezijwegel.betteryaml.validation.validator.Validator;
import org.jetbrains.annotations.NotNull;

public class ColorCharValidator extends Validator
{
    @Override
    public Object validate(@NotNull Object o)
    {
        if (! (o instanceof String))
        {
            return "&f";
        }

        String string = (String) o;
        return string.replace("&", "§");
    }
}
