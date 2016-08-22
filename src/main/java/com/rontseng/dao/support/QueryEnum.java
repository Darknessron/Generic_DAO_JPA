/*
 * Copyright (c), Audatex GmbH, Switzerland. This is UNPUBLISHED
 * PROPRIETARY SOURCE CODE of Audatex GmbH; the contents of this file
 * may not be disclosed to third parties, copied or duplicated in any form, in
 * whole or in part, without the prior written permission of Audatex 
 * GmbH. ALL RIGHTS RESERVED.
 */
package com.rontseng.dao.support;

/**
 * Describe the type here.
 *
 * @author Ron
 * @see AXN-
 * @date Mar 21, 2016 11:53:18 AM
 */
public enum QueryEnum {
  /**
   * 等於
   */
  EQUAL,
  /**
   * 大於
   */
  GREATER_THAN,
  /**
   * 大於等於
   */
  GREATER_EQUAL_THAN,
  /**
   * 小於
   */
  LESS_THAN,
  /**
   * 小於等於
   */
  LESS_EQUAL_THAN,
  /**
   * 不等於
   */
  NOT_EQUAL,
  /**
   * 於列表內
   */
  IN,
  /**
   * 不於列表內
   */
  NOT_IN,
  /**
   * 字首相符
   */
  RIGHT_LIKE,
  /**
   * 不包含字首相符
   */
  NOT_RIGHT_LIKE,
  /**
   * 字尾相符
   */
  LEFT_LIKE,
  /**
   * 不包含字尾相符
   */
  NOT_LEFT_LIKE,
  /**
   * 部分相符
   */
  ALL_LIKE,
  /**
   * 不包含部分相符
   */
  NOT_ALL_LIKE,
  /**
   * 在兩個日期之間
   */
  BETWEEN,
  /**
   * 不在兩個日期之間
   */
  NOT_BETWEEN,
  /**
   * 或
   */
  OR;
}
