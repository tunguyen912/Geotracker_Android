package ca.georgebrown.comp3074.comp3074_project;

public class Member {
    private String name;
    private int stuId;
    public Member(String name,int stuId) {
        this.name = name;
        this.stuId = stuId;
    }
    public static final Member[] members = {
            new Member("Vo Anh Tu Nguyen", 101148412),
            new Member("Thanh Quan", 101142560),
            new Member("Cong Nhat Quang Pham", 101136246),
            new Member("Thong Nguyen", 101140366)
    };

    public String getName() {
        return name;
    }

    public int getStuId() {
        return stuId;
    }
}
