package com.orientechnologies.orient.core.sql.executor;

import com.orientechnologies.common.concur.OTimeoutException;
import com.orientechnologies.orient.core.command.OCommandContext;
import com.orientechnologies.orient.core.index.OIndex;
import com.orientechnologies.orient.core.sql.parser.OIndexIdentifier;

import java.util.Map;
import java.util.Optional;

/**
 * Created by luigidellaquila on 17/03/17.
 */
public class CountFromIndexStep extends AbstractExecutionStep {
  private final OIndexIdentifier target;
  private final String           alias;

  private long count = 0;

  private boolean executed = false;

  public CountFromIndexStep(OIndexIdentifier targetIndex, String alias, OCommandContext ctx, boolean profilingEnabled) {
    super(ctx, profilingEnabled);
    this.target = targetIndex;
    this.alias = alias;
  }

  @Override
  public OResultSet syncPull(OCommandContext ctx, int nRecords) throws OTimeoutException {
    getPrev().ifPresent(x -> x.syncPull(ctx, nRecords));

    return new OResultSet() {
      @Override
      public boolean hasNext() {
        return !executed;
      }

      @Override
      public OResult next() {
        if (executed) {
          throw new IllegalStateException();
        }
        long begin = profilingEnabled ? System.nanoTime() : 0;
        try {
          OIndex<?> idx = ctx.getDatabase().getMetadata().getIndexManager().getIndex(target.getIndexName());
          long size = idx.getSize();
          executed = true;
          OResultInternal result = new OResultInternal();
          result.setProperty(alias, size);
          return result;
        } finally {
          count += (System.nanoTime() - begin);
        }
      }

      @Override
      public void close() {

      }

      @Override
      public Optional<OExecutionPlan> getExecutionPlan() {
        return null;
      }

      @Override
      public Map<String, Long> getQueryStats() {
        return null;
      }

      @Override
      public void reset() {
        CountFromIndexStep.this.reset();
      }
    };
  }

  @Override
  public void reset() {
    executed = false;
  }

  @Override
  public String prettyPrint(int depth, int indent) {
    String spaces = OExecutionStepInternal.getIndent(depth, indent);
    return spaces + "+ CALCULATE INDEX SIZE: " + target;
  }
}
