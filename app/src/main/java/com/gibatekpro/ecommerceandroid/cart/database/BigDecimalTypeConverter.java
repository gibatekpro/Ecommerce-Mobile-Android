package com.gibatekpro.ecommerceandroid.cart.database;
import androidx.room.TypeConverter;
import java.math.BigDecimal;

public class BigDecimalTypeConverter {
    @TypeConverter
    public static BigDecimal fromDouble(Double value) {
        return value == null ? null : new BigDecimal(value);
    }

    @TypeConverter
    public static Double toDouble(BigDecimal value) {
        return value == null ? null : value.doubleValue();
    }
}