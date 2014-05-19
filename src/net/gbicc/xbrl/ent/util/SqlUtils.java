package net.gbicc.xbrl.ent.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import java.util.LinkedList;
import java.util.List;

import net.gbicc.xbrl.ent.model.BasicInfo;
import net.gbicc.xbrl.ent.model.PublicAccount;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SqlUtils {

	private static final Log log = LogFactory.getLog(SqlUtils.class);

	public static List<String> getTableColumns(String tableName) {
		Connection conn = null;
		Statement st = null;
		List<String> columns = new ArrayList<String>();
		try {
			conn = ConnectionUtil.getConnection();
			conn.createStatement();
			String sql = "select COLUMN_NAME from user_tab_columns "
					+ "where table_name = '" + tableName + "'";
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				columns.add(rs.getString(0));
			}
			return columns;
		} catch (SQLException sqlex) {
			log.info(sqlex.getMessage());
			return null;
		} finally {
			try {
				if (st != null) {
					st.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqlex) {
				// e.printStackTrace();
				log.info(sqlex.getMessage());
			}
		}
	}

	/**
	 * 查找表名下对应 的元素和字段
	 * 
	 * @param name
	 * @param type
	 * @return
	 */
	public static List<BasicInfo> getBasicInfos(String name, String type) {
		List list = new LinkedList();
		Connection conn = null;
		Statement st = null;
		String sql = "select elementName,fieldName ,tableName from "
				+ "TD_RELATION t where t.tablename in("
				+ "select tableName from"
				+ " TD_RELATION  where elementName = '" + name
				+ "') and fieldname is not null and type ='" + type + "'";
		try {
			conn = ConnectionUtil.getConnection();
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				BasicInfo basicInfo = new BasicInfo();
				basicInfo.setElementName(rs.getString("elementName"));
				basicInfo.setFieldName(rs.getString("fieldName"));
				basicInfo.setTableName(rs.getString("tableName"));
				list.add(basicInfo);
			}
			return list;
		} catch (Exception e) {
			// e.printStackTrace();
			return null;
		} finally {
			try {
				if (st != null) {
					st.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 执行SQL
	 * 
	 * @param sql
	 * @return
	 */
	public static boolean executeSql(String sql) {
		boolean flag = false;
		Connection conn = null;
		Statement st = null;
		try {
			conn = ConnectionUtil.getConnection();
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		} finally {
			try {
				if (st != null) {
					st.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return flag;

	}

	public static List getBasicInfoByType(String type) {
		List list = new ArrayList();
		Connection conn = null;
		Statement st = null;
		String sql = "select distinct(tableName) from TD_RELATION t"
				+ " where t.type = '" + type + "' and t.fieldname is null";
		try {
			conn = ConnectionUtil.getConnection();
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				list.add(rs.getString("tableName"));
			}
			return list;
		} catch (Exception e) {
			// e.printStackTrace();
			return null;
		} finally {
			try {
				if (st != null) {
					st.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static List<PublicAccount> getPartenS(String tableName) {
		Connection conn = null;
		Statement st = null;
		List list = new LinkedList();
		String sql = " select ACCT_CODE,ACCT_CNAME,ACCT_ENAME,ACCT_CLASS,REMARK_INFO "
				+ "from TD_PUBLIC_ACCOUNT where ACCT_CODE in"
				+ "(select t.parten from td_relation t where t.fieldname is not null and t.tablename='"
				+ tableName + "'group by t.parten) and ACCT_ENAME is null";
		try {
			conn = ConnectionUtil.getConnection();
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				PublicAccount publicAccount = new PublicAccount();
				publicAccount.setCode(rs.getString("ACCT_CODE"));
				publicAccount.setcName(rs.getString("ACCT_CNAME").toString());
				publicAccount.seteName(rs.getString("ACCT_ENAME"));
				publicAccount.setTableName(rs.getString("ACCT_CLASS"));
				publicAccount.setRemarkInfo(rs.getString("REMARK_INFO"));
				list.add(publicAccount);
			}
			return list;
		} catch (Exception e) {
			// e.printStackTrace();
			return null;
		} finally {
			try {
				if (st != null) {
					st.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static List<BasicInfo> getBasicInfosItem(String tableName,
			String parten) {
		Connection conn = null;
		Statement st = null;
		List list = new LinkedList();
		String sql = "select elementName,fieldName ,tableName from "
				+ "TD_RELATION t where t.tablename ='" + tableName
				+ "' and t.parten ='" + parten + "'";
		try {
			conn = ConnectionUtil.getConnection();
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				BasicInfo basicInfo = new BasicInfo();
				basicInfo.setElementName(rs.getString("elementName"));
				basicInfo.setFieldName(rs.getString("fieldName"));
				basicInfo.setTableName(rs.getString("tableName"));
				list.add(basicInfo);
			}
			return list;
		} catch (Exception e) {
			// e.printStackTrace();
			return null;
		} finally {
			try {
				if (st != null) {
					st.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 查找具有相同表名的TUPLE的元素个数
	 * 
	 * @param name
	 * @return
	 */
	public static int getTuples(String name) {
		Connection conn = null;
		Statement st = null;
		String sql = " select count(*) from td_relation where tableName in("
				+ "select  t.tablename from td_relation t where t.elementname ='"
				+ name + "' )" + "and type='tuple' and fieldname is null";
		try {
			conn = ConnectionUtil.getConnection();
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			int tupleSize = 0;
			if (rs.next()) {
				tupleSize++;
			}
			return tupleSize;
		} catch (Exception e) {
			// e.printStackTrace();
			return 0;
		} finally {
			try {
				if (st != null) {
					st.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static List<BasicInfo> getBasicInfoByAccount(String name, String type) {
		Connection conn = null;
		Statement st = null;
		List list = new LinkedList();
		String sql = "select elementName,fieldName ,tableName,a.acct_cname parten "
				+ "from td_relation r ,td_public_account a where "
				+ "  r.parten = a.acct_code and tableName in("
				+ "select t.tablename from td_relation t where t.elementname ='"
				+ name
				+ "')"
				+ "and parten in("
				+ "select a.acct_code from td_public_account a where a.acct_ename='"
				+ name + "') and type ='" + type + "'";
		try {
			conn = ConnectionUtil.getConnection();
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);

			if (rs.next()) {
				BasicInfo basicInfo = new BasicInfo();
				basicInfo.setElementName(rs.getString("elementName"));
				basicInfo.setFieldName(rs.getString("fieldName"));
				basicInfo.setTableName(rs.getString("tableName"));
				basicInfo.setParten(rs.getString("parten"));
				list.add(basicInfo);
			}
			return list;
		} catch (Exception e) {
			// e.printStackTrace();
			return null;
		} finally {
			try {
				if (st != null) {
					st.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 执行多个SQL，最后提交
	 * 
	 * @param sqls
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */

	public static void executeSQLs(List sqlList) throws Exception {
		Connection con = null;
		Statement stmt = null;
		try {
			// 创建连接
			con = ConnectionUtil.getConnection();
			con.setAutoCommit(false);
			stmt = con.createStatement();
			StringBuffer sb = new StringBuffer();
			for (int ii = 0; ii < sqlList.size(); ii++) {
				if (sqlList.get(ii) != null) {
					stmt.addBatch((String) sqlList.get(ii));
				}
			}
			stmt.executeBatch();
			con.commit();
			stmt.close();
			con.close();
			stmt = null;
			con = null;
		} catch (Exception e) {
			con.rollback();
			throw e;
		} finally {
			try {
				if (con != null) {
					con.close();
					con = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (Exception e) {
				throw e;
			}
		}

	}
}
