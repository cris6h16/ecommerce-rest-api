//package org.cris6h16;
//
//import jakarta.persistence.FetchType;
//import jakarta.persistence.ForeignKey;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//
//public class OrderEntity {
//
//    @ManyToOne(
//            fetch = FetchType.LAZY,
//            cascade = {}
//    )
//    @JoinColumn(
//            name = "user_id",
//            foreignKey = @ForeignKey(name = "fk_order_user_id")
//    )
//    private UserEntity user;
//}
