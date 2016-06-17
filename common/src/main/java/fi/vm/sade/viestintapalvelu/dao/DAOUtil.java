package fi.vm.sade.viestintapalvelu.dao;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.StringPath;
import fi.vm.sade.viestintapalvelu.common.util.CollectionHelper;
import org.hibernate.internal.util.StringHelper;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class DAOUtil {
    public static final int MAX_CHUNK_SIZE_FOR_IN_EXPRESSION = 1000;

    public static String splittedInExpression(List<String> values, final String hqlColumn, final Map<String, Object> params, final String valPrefix) {
        final List<List<String>> oidChunks = CollectionHelper.split(values, MAX_CHUNK_SIZE_FOR_IN_EXPRESSION);
        final AtomicInteger n = new AtomicInteger(0);
        Collection<String> inExcepssionsCollection = Collections2.transform(oidChunks, new Function<List<String>, String>() {
            public String apply(@Nullable List<String> oidsChunk) {
                int pNum = n.incrementAndGet();
                String paramName = valPrefix + "_" + pNum;
                params.put(paramName, oidsChunk);
                return hqlColumn + " in (:" + paramName + ")";
            }
        });
        return StringHelper.join(" OR ", inExcepssionsCollection.toArray(new String[inExcepssionsCollection.size()]));
    }

    public static BooleanExpression[] splittedInExpression(List<String> values, final StringPath column) {
        List<List<String>> oidChunks = CollectionHelper.split(values, MAX_CHUNK_SIZE_FOR_IN_EXPRESSION);
        Collection<BooleanExpression> inExcepssionsCollection = Collections2.transform(oidChunks, new Function<List<String>, BooleanExpression>() {
            public BooleanExpression apply(@Nullable List<String> oidsChunk) {
                return column.in(oidsChunk);
            }
        });
        return inExcepssionsCollection.toArray(new BooleanExpression[inExcepssionsCollection.size()]);
    }
}
