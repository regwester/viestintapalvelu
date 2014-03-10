package fi.vm.sade.viestintapalvelu.jalkiohjauskirje;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang.StringUtils.isEmpty;

public class ContainsKeysValidator implements ConstraintValidator<ContainsKeys, List<Map<String, String>>> {

    String[] requiredKeys;

    @Override
    public void initialize(final ContainsKeys constraintAnnotation) {
        requiredKeys = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(final List<Map<String, String>> values, final ConstraintValidatorContext context) {
        if (values == null) {
            return false;
        }
        boolean result = true;
        Map<String, String> tulos;
        for (int i = 0; i < values.size(); i++) {
            tulos = values.get(i);
            for (String requiredKey : requiredKeys) {
                if (!tulos.containsKey(requiredKey) || isEmpty(tulos.get(requiredKey))) {
                    context.buildConstraintViolationWithTemplate("Missing required key tulokset[" + i + "]." + requiredKey)
                            .addNode(requiredKey).addConstraintViolation().disableDefaultConstraintViolation();
                    result &= false;
                }
            }
        }

        return result;
    }
}
