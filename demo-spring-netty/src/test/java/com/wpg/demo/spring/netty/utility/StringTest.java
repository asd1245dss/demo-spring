package com.wpg.demo.spring.netty.utility;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author ChangWei Li
 * @version 2017-09-13 11:42
 */
public class StringTest {

    @Test
    public void testSplit() {
        String str = "{\"deviceId\":\"16070046-2\",\"timestamp\":\"2017-09-13 11:39:31\",\"values\":{\"CumulativeFlow\":0.0,\"GivenPressure\":83.0,\"HighPressureAlarm\":0.0,\"IncreasedPumpTime\":30.0,\"InletPressure\":17.0,\"InletRange\":100.0,\"InstantaneousFlow\":0.0,\"Integra\":0.0,\"InvertedPumpTime\":240.0,\"NegativeAlarm\":0.0,\"NegativePressureCapValue\":12.0,\"NegativePressureLimitValue\":8.0,\"NegativeProtectionPressure\":0.0,\"No1FrequencyCurrent\":0.0,\"No1FrequencyFailure\":0.0,\"No1FrequencyOper\":0.0,\"No1FrequencyOutput\":43.0,\"No1FrequencyOutputVoltage\":0.0,\"No1FrequencyPower\":0.0,\"No1FrequencyRunningTime\":0.0,\"No1FrequencySpeed\":0.0,\"No1FrequencyTemp\":30.0,\"No1FrequencyTotalKWH\":2040.0,\"No1PowerFrequencyOper\":0.0,\"No1PumpFailureAlarm\":0.0,\"No1RunningTime\":0.0,\"No2FrequencyCurrent\":0.0,\"No2FrequencyFailure\":0.0,\"No2FrequencyOper\":1.0,\"No2FrequencyOutput\":0.0,\"No2FrequencyOutputVoltage\":0.0,\"No2FrequencyPower\":0.0,\"No2FrequencySpeed\":0.0,\"No2FrequencyTemp\":30.0,\"No2PowerFrequencyOper\":0.0,\"No2PumpFailureAlarm\":0.0,\"No2RunningTime\":0.0,\"No3FrequencyCurrent\":10.0,\"No3FrequencyFailure\":0.0,\"No3FrequencyOper\":0.0,\"No3FrequencyOutput\":0.0,\"No3FrequencyOutputVoltage\":0.0,\"No3FrequencyPower\":0.0,\"No3FrequencyRunningTime\":0.0,\"No3FrequencySpeed\":2487.0,\"No3FrequencyTemp\":0.0,\"No3FrequencyTotalKWH\":1796.0,\"No3PowerFrequencyOper\":0.0,\"No3PumpFailureAlarm\":0.0,\"No3RunningTime\":185.0,\"NoWaterAlarm\":0.0,\"OutletPressure\":83.0,\"OutletRange\":160.0,\"OveFlowProtection\":0.0,\"OverPressureAlarm\":0.0,\"PressureCapValue\":87.0,\"PressureLimitValue\":60.0,\"Proportion\":0.0,\"ReducedFrequency\":0.0,\"ReducedPumpTime\":5.0,\"SleepTime\":100.0,\"SleepingFrequency\":410.0,\"StopAlarm\":0.0,\"Tanklevel\":0.0,\"UnderPressureAlarm\":0.0,\"WakeUpPressure\":79.0,\"WaterFloodedAlarm\":0.0}}";
        System.out.println(Arrays.asList(str.split("\\W+")));
    }

    @Test
    public void test_stream_operation_supplier() {
        Stream<Long> numberStream = Stream.generate(new Supplier<Long>() {

            long value = 0;

            @Override
            public Long get() {
                return value++;
            }
        });

        System.out.println(numberStream.limit(50).collect(Collectors.averagingLong((num) -> num)).longValue());
    }

    @Test
    public void test_stream_operation_map() {
        List<String> list = Arrays.asList("a", "b", "c", "d", "e");
        list = list.stream().map(v -> v + 111).collect(Collectors.toList());
        System.out.println(list);
    }

    @Test
    public void test_stream_operation_flatmap() {
        List<String> list = Arrays.asList("a", "b", "c", "d", "e");
        list = list.stream().map(v -> v + 111).collect(Collectors.toList());
        System.out.println(list);
    }

    @Test
    public void test_stream_operation_filter() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
        System.out.println(list.stream().filter(v -> v % 3 == 0).collect(Collectors.toList()));
    }

}
