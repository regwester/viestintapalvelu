package fi.vm.sade.viestintapalvelu.jalkiohjauskirje;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.net.MediaType;

import javax.validation.ConstraintViolation;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.Set;

public class IllegalJalkiohjauskirjeException extends WebApplicationException {

    public IllegalJalkiohjauskirjeException(Set<ConstraintViolation<JalkiohjauskirjeBatch>> violations) {
        super(Response.status(Response.Status.BAD_REQUEST)
                .entity(
                        Lists.newArrayList(Iterables.transform(violations, new Function<ConstraintViolation<JalkiohjauskirjeBatch>, Map<String, String>>() {
                            @Override
                            public Map<String, String> apply(ConstraintViolation<JalkiohjauskirjeBatch> jalkiohjauskirjeBatchConstraintViolation) {
                                return ImmutableMap.of(
                                        "message", jalkiohjauskirjeBatchConstraintViolation.getMessage(),
                                        "propertyPath", jalkiohjauskirjeBatchConstraintViolation.getPropertyPath().toString());
                            }
                        }))

                ).type(MediaType.JSON_UTF_8.toString()).build());
    }
}
