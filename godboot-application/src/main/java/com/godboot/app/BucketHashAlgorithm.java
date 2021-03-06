package com.leetcode.hash.weight;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Data
@Slf4j
public class BucketHashAlgorithm {
    /**
     * 计算hash权重
     *
     * @param maxHashNumberLength hash数字最大长度
     * @param buckets             桶列表
     * @return 权重封装
     */
    public BucketHashWeight computeHashWeight(Integer maxHashNumberLength, List<BucketHash> buckets) {
        BucketHashWeight hashWeight = new BucketHashWeight();

        hashWeight.setBuckets(buckets);

        if (CollectionUtils.isEmpty(buckets)) {
            return hashWeight;
        }

        Integer totalHashNumberWeight = new BigDecimal(Math.pow(10D, maxHashNumberLength)).setScale(0, BigDecimal.ROUND_UP).intValue();

        /**
         * 存储hash长度权重值
         */
        Map<Integer, Integer> numberLengthWeightSet = new HashMap<>();
        for (Integer i = 1; i <= maxHashNumberLength; i++) {
            numberLengthWeightSet.put(i, new BigDecimal(Math.pow(10D, maxHashNumberLength - i)).setScale(0, BigDecimal.ROUND_UP).intValue());
        }

        AtomicInteger totalWeight = new AtomicInteger(0);
        StringBuffer hash = new StringBuffer();
        Set<String> hashNumberSet = new HashSet<>();

        String defaultHashNumbers = "0,1,2,3,4,5,6,7,8,9";

        Map<String, String> defaultHashNumberSet = Arrays.asList(defaultHashNumbers.split(",")).stream().collect(Collectors.toMap(s -> s, o -> o, (o, o2) -> o2));

        buckets.stream().forEach(bucketHash -> {
            /**
             * 如果是兜底桶执行兜底桶逻辑
             */
            if (Integer.valueOf("1").equals(bucketHash.getBackUp())) {
                doBackUpBucketHash(bucketHash, maxHashNumberLength, totalHashNumberWeight, numberLengthWeightSet, new HashSet<>(), new HashMap<>());
            } else {
                /**
                 * 处理非兜底桶逻辑
                 */
                doBucketHash(bucketHash, maxHashNumberLength, totalHashNumberWeight, numberLengthWeightSet, hashNumberSet, defaultHashNumberSet);

                totalWeight.getAndAdd(bucketHash.getWeight());
                hash.append("," + bucketHash.getHash());
            }
        });

        List<BucketHash> addBuckets = buckets.stream().filter(bucketHash -> StringUtils.equalsIgnoreCase("add", bucketHash.getOperate()) && StringUtils.isBlank(bucketHash.getHash())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(addBuckets)) {
            List<String> optionalHashList = defaultHashNumberSet.keySet().stream().collect(Collectors.toList());
            addBuckets.stream().forEach(bucketHash -> {
                /**
                 * 权重不能超过100%
                 */
                int tempTotalWeight = totalWeight.get() + numberLengthWeightSet.get(1);

                StringBuilder hashNumberBuilder = new StringBuilder();
                Integer weight = 0;

                while (tempTotalWeight <= totalHashNumberWeight && defaultHashNumberSet.size() > 0) {
                    String remove = optionalHashList.remove(0);
                    hashNumberBuilder.append(remove + ",");
                    weight += numberLengthWeightSet.get(1);

                    defaultHashNumberSet.remove(remove);

                    totalWeight.getAndAdd(numberLengthWeightSet.get(1));
                    tempTotalWeight += numberLengthWeightSet.get(1);
                }

                String tempHash = StringUtils.removeEnd(hashNumberBuilder.toString(), ",");

                bucketHash.setHash(tempHash);
                bucketHash.setWeight(weight);
                bucketHash.setWeightPercent(new BigDecimal(Float.valueOf(String.valueOf(bucketHash.getWeight())) * 100 / totalHashNumberWeight).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());

                hash.append(tempHash);
            });
        }

        String totalHash = StringUtils.removeStart(hash.toString(), ",");
        hashWeight.setTotalHash(totalHash);
        hashWeight.setOptionalHash(StringUtils.join(defaultHashNumberSet.keySet().stream().collect(Collectors.toList()), ","));

        hashWeight.setTotalWeight(totalWeight.get());
        hashWeight.setTotalWeightPercent(new BigDecimal(Float.valueOf(String.valueOf(hashWeight.getTotalWeight())) * 100 / totalHashNumberWeight).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
        hashWeight.setPassWeight(hashWeight.getTotalWeight() <= totalHashNumberWeight);

        return hashWeight;
    }

    private BucketHash doBackUpBucketHash(BucketHash bucketHash, Integer maxHashNumberLength, Integer totalHashNumberWeight, Map<Integer, Integer> numberLengthWeightSet, Set<String> hashNumberSet, Map<String, String> defaultHashNumberSet) {
        return doBucketHash(bucketHash, maxHashNumberLength, totalHashNumberWeight, numberLengthWeightSet, hashNumberSet, defaultHashNumberSet);
    }

    /**
     * @param bucketHash            hash桶
     * @param maxHashNumberLength   允许输入最大长度
     * @param totalHashNumberWeight 总权重值
     */
    private BucketHash doBucketHash(BucketHash bucketHash, Integer maxHashNumberLength, Integer totalHashNumberWeight, Map<Integer, Integer> numberLengthWeightSet, Set<String> hashNumberSet, Map<String, String> defaultHashNumberSet) {
        String hash = bucketHash.getHash();

        bucketHash.setWeight(0);
        bucketHash.setWeightPercent(0F);

        if (StringUtils.isBlank(hash) && StringUtils.equalsIgnoreCase("add", bucketHash.getOperate())) {
            return bucketHash;
        }

        String[] hashNumbers = hash.split(",");

        StringBuilder hashNumberBuilder = new StringBuilder();
        Integer weight = 0;

        for (String hashNumber : hashNumbers) {
            if (StringUtils.isBlank(hashNumber)) {
                continue;
            }

            if (StringUtils.isBlank(StringUtils.trim(hashNumber))) {
                throw new RuntimeException("hash值不能包含空格");
            }

            hashNumber = StringUtils.trim(hashNumber);

            int length = StringUtils.length(hashNumber);
            if (length > maxHashNumberLength) {
                throw new RuntimeException("hash值最大长度不允许超过" + maxHashNumberLength + "字符，hash值：" + hashNumber);
            }

            try {
                Integer.parseInt(hashNumber);
            } catch (NumberFormatException e) {
                throw new RuntimeException("hash值只能包含数字，检测到非数字输入值，hash值：" + hashNumber);
            }

            /**
             * 是否已经包含
             */
            boolean contains = hashNumberSet.contains(hashNumber);
            if (contains) {
                throw new RuntimeException("检测到重复的hash值，hash值：" + hashNumber);
            }

            hashNumberSet.add(hashNumber);

            /**
             * 默认中移除
             */
            defaultHashNumberSet.remove(hashNumber);

            weight += numberLengthWeightSet.get(length);
            hashNumberBuilder.append(hashNumber + ",");
        }

        hash = StringUtils.removeEnd(hashNumberBuilder.toString(), ",");

        bucketHash.setWeight(weight);
        bucketHash.setWeightPercent(new BigDecimal(Float.valueOf(String.valueOf(bucketHash.getWeight())) * 100 / totalHashNumberWeight).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
        bucketHash.setHash(hash);

        hashNumberBuilder.delete(0, hashNumberBuilder.length() - 1);

        return bucketHash;
    }
}
     
