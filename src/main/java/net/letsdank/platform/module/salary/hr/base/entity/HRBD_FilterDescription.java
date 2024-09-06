package net.letsdank.platform.module.salary.hr.base.entity;

/**
 *
 * @param leftValue Left value
 * @param compareType Allowed compare type are same that uses in queries.
 * @param rightValue Any value, allowed in filter of queries.
 * @param relativePath If <code>true</code>, then in <code>leftValue</code> parameter
 *                     should contains dimension name, resource or registry.
 */
public record HRBD_FilterDescription(String leftValue, String compareType, Object rightValue, boolean relativePath) {

}
