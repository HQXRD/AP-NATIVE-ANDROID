package com.xtree.mine.vo;

public class GameMenusVo implements Comparable<GameMenusVo>{
    public int cid;
    public String name;
    public String key;
    public boolean needTransfer = false;

    @Override
    public int compareTo(GameMenusVo vo) {
        return this.name.compareTo(vo.key);
    }
}
