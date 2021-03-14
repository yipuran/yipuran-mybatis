package org.yipuran.mybatis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * ORDER BY 句のためのパラーメータ抽象クラス.
 * <PRE>
 *  &lt;select id="orderBySelect" parameterType="ItemCondition" resultType="jp.ibatis3model.entity.Item">
 *  SELECT * FROM tblitem
 *  WHERE price > #{price}
 *  &lt;if test="sizeList > 0">
 *      &lt;foreach collection="orderList" item="orderby" open="ORDER BY" close="" separator=",">
 *      ${orderby.field} ${orderby.type}
 *      &lt;/foreach>
 *  &lt;/if>
 *  &lt;/select>
 *
 * public class ItemCondition extends AbstractOrderByConditon{
 *     public int price;
 * }
 * </PRE>
 */
public abstract class AbstractOrderByConditon{
	/**
	 * ソートキーリスト返す.
	 */
   public Collection<SortKey> orderList;

   Integer sizeList;

   /**
    * defaulte constrcutor.
    */
   public AbstractOrderByConditon(){
      this.orderList = new ArrayList<SortKey>();
   }
   /**
    * constrcutor.
    * @param orderList ソートキーリスト
    */
   public AbstractOrderByConditon(List<SortKey> orderList){
      this.orderList = new ArrayList<SortKey>(orderList);
   }
   /**
    * ソートキー.
    * @param field ソートキー
    * @param type 順序タイプ
    */
   public void addOrderBy(String field, OrderBy type){
      this.orderList.add(new SortKey(field, type.name()));
      this.sizeList = this.orderList.size();
   }
}
