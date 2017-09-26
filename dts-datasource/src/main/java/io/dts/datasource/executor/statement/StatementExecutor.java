/*
 * Copyright 1999-2015 dangdang.com.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package io.dts.datasource.executor.statement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import io.dts.datasource.executor.BaseStatementUnit;
import io.dts.datasource.executor.ExecuteCallback;
import io.dts.datasource.executor.ExecutorEngine;
import lombok.RequiredArgsConstructor;

/**
 * Statement Executor for multiple threads.
 * 
 * @author gaohongtao
 * @author caohao
 * @author zhangliang
 */
@RequiredArgsConstructor
public final class StatementExecutor {
    
    private final ExecutorEngine executorEngine;

    private final StatementUnit statementUnit;
    
    /**
     * Execute query.
     * 
     * @return result set list
     */
    public ResultSet executeQuery() throws Exception {
        return executorEngine.executeStatement(statementUnit, new ExecuteCallback<ResultSet>() {
            
            @Override
            public ResultSet execute(final BaseStatementUnit baseStatementUnit) throws Exception {
                return baseStatementUnit.getStatement().executeQuery(baseStatementUnit.getSqlExecutionUnit().getSql());
            }
        });
    }
    
    /**
     * Execute update.
     * 
     * @return effected records count
     */
    public int executeUpdate() throws Exception {
        return executeUpdate(new Updater() {
            
            @Override
            public int executeUpdate(final Statement statement, final String sql) throws SQLException {
                return statement.executeUpdate(sql);
            }
        });
    }
    
    /**
     * Execute update with auto generated keys.
     * 
     * @param autoGeneratedKeys auto generated keys' flag
     * @return effected records count
     */
    public int executeUpdate(final int autoGeneratedKeys) throws Exception {
        return executeUpdate(new Updater() {
            
            @Override
            public int executeUpdate(final Statement statement, final String sql) throws SQLException {
                return statement.executeUpdate(sql, autoGeneratedKeys);
            }
        });
    }
    
    /**
     * Execute update with column indexes.
     *
     * @param columnIndexes column indexes
     * @return effected records count
     */
    public int executeUpdate(final int[] columnIndexes) throws Exception {
        return executeUpdate(new Updater() {
            
            @Override
            public int executeUpdate(final Statement statement, final String sql) throws SQLException {
                return statement.executeUpdate(sql, columnIndexes);
            }
        });
    }
    
    /**
     * Execute update with column names.
     *
     * @param columnNames column names
     * @return effected records count
     */
    public int executeUpdate(final String[] columnNames) throws Exception {
        return executeUpdate(new Updater() {
            
            @Override
            public int executeUpdate(final Statement statement, final String sql) throws SQLException {
                return statement.executeUpdate(sql, columnNames);
            }
        });
    }
    
    private int executeUpdate(final Updater updater) throws Exception {
        Integer results = executorEngine.executeStatement(statementUnit, new ExecuteCallback<Integer>() {
            
            @Override
            public Integer execute(final BaseStatementUnit baseStatementUnit) throws Exception {

                return updater.executeUpdate(baseStatementUnit.getStatement(), baseStatementUnit.getSqlExecutionUnit().getSql());
            }
        });
        return results;
    }

    
    /**
     * Execute SQL.
     *
     * @return return true if is DQL, false if is DML
     */
    public boolean execute() throws Exception {
        return execute(new Executor() {
            
            @Override
            public boolean execute(final Statement statement, final String sql) throws SQLException {
                return statement.execute(sql);
            }
        });
    }
    
    /**
     * Execute SQL with auto generated keys.
     *
     * @param autoGeneratedKeys auto generated keys' flag
     * @return return true if is DQL, false if is DML
     */
    public boolean execute(final int autoGeneratedKeys) throws Exception {
        return execute(new Executor() {
            
            @Override
            public boolean execute(final Statement statement, final String sql) throws SQLException {
                return statement.execute(sql, autoGeneratedKeys);
            }
        });
    }
    
    /**
     * Execute SQL with column indexes.
     *
     * @param columnIndexes column indexes
     * @return return true if is DQL, false if is DML
     */
    public boolean execute(final int[] columnIndexes) throws Exception {
        return execute(new Executor() {
            
            @Override
            public boolean execute(final Statement statement, final String sql) throws SQLException {
                return statement.execute(sql, columnIndexes);
            }
        });
    }
    
    /**
     * Execute SQL with column names.
     *
     * @param columnNames column names
     * @return return true if is DQL, false if is DML
     */
    public boolean execute(final String[] columnNames) throws Exception {
        return execute(new Executor() {
            
            @Override
            public boolean execute(final Statement statement, final String sql) throws SQLException {
                return statement.execute(sql, columnNames);
            }
        });
    }
    
    private boolean execute(final Executor executor) throws Exception {
        Boolean result = executorEngine.executeStatement(statementUnit, new ExecuteCallback<Boolean>() {
            
            @Override
            public Boolean execute(final BaseStatementUnit baseStatementUnit) throws Exception {
                return executor.execute(baseStatementUnit.getStatement(), baseStatementUnit.getSqlExecutionUnit().getSql());
            }
        });
        if (null == result) {
            return false;
        }
        return result;
    }
    
    private interface Updater {
        
        int executeUpdate(Statement statement, String sql) throws SQLException;
    }
    
    private interface Executor {
        
        boolean execute(Statement statement, String sql) throws SQLException;
    }
}
