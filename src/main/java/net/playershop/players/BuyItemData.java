package net.playershop.players;

import net.playershop.shop.BaseSubClass;
import net.playershop.shop.Items;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author SmallasWater
 * Create on 2021/2/2 11:28
 * Package net.playershop.players
 */
public class BuyItemData {
    private FakeItems buyItemName;

    private int buyCount;

    private Date lastBuyTime;

    public BuyItemData(Items buyItemName){
        this.buyItemName = new FakeItems(buyItemName);
        this.buyCount = 0;
        this.lastBuyTime = new Date();
    }

    private BuyItemData(FakeItems buyItemName,int buyCount,Date lastBuyTime){
        this.buyItemName = buyItemName;
        this.buyCount = buyCount;
        this.lastBuyTime = lastBuyTime;
    }

    public int getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(int buyCount) {
        this.buyCount = buyCount;
    }

    public Date getLastBuyTime() {
        return lastBuyTime;
    }

    public FakeItems getBuyItemName() {
        return buyItemName;
    }

    @Override
    public String toString() {
        return buyItemName.toString()+ "buyCount: "+buyCount+" lastBuyTime:"+toDateString(lastBuyTime);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof BuyItemData){
            return ((BuyItemData) obj).getBuyItemName().equals(buyItemName);
        }
        return false;
    }

    /** 将 2019/6/9/24/11 格式的string转换为 Date
     * @param format 时间格式
     *
     * @return 时间类*/
    private static Date getDate(String format){
        SimpleDateFormat lsdStrFormat = new SimpleDateFormat("yyyy/MM/dd");
        try{
            return lsdStrFormat.parse(format);
        } catch (ParseException ex) {
            return null;
        }
    }
    /** 获取相差天数
     * @param oldData 时间1
     *
     * @return 天数*/

    public static int getTime(Date oldData) {
        long temp = System.currentTimeMillis() - oldData.getTime();
        return (int) temp / 1000 / (60 * 60 * 24);
    }

    /** 将Date 转换 String*/
    private static String toDateString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        return sdf.format(date);
    }

    void onRunTime(){
        int day = getTime(lastBuyTime);
        if(day >= buyItemName.getResetTime()){
            buyCount = 0;
            lastBuyTime = new Date();
        }

    }
    public static BuyItemData getInstance(Map map){
        int buyCount = (int) map.get("PlayerBuyCount");
        Date lastBuyTime = getDate(map.get("PlayerLastBuyDate").toString());
        FakeItems items = FakeItems.getFakeItems((Map) map.get("data"));
        return new BuyItemData(items,buyCount,lastBuyTime);

    }

    LinkedHashMap<String,Object> getSaveMap(){
        LinkedHashMap<String,Object> map = new LinkedHashMap<>();
        map.put("PlayerBuyCount",buyCount);
        map.put("PlayerLastBuyDate",toDateString(lastBuyTime));
        map.put("data",buyItemName.getSaveMap());
        return map;
    }
}
