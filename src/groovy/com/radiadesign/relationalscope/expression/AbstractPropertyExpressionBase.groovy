package com.radiadesign.relationalscope.expression

import com.radiadesign.relationalscope.RelationalScopeBuilder
import com.radiadesign.relationalscope.RelationalScope

class AbstractPropertyExpressionBase extends ExpressionBase {
  
  def propertyKey
  def builder
  
  AbstractPropertyExpressionBase(_propertyKey, RelationalScopeBuilder _builder) {
    propertyKey = _propertyKey
    builder = _builder
  }
  
  static String fullPropertyNameFor(Map options, String propertyName) {
    def alias
    if (options.associationName) {
      def discriminator = RelationalScope.aliasDiscriminatorFor(options)
      alias = options.associationAliases[discriminator][options.associationName]
      assert alias : "An association was used for which no alias has been created"
    }
    return "${alias ?: options.currentRootAlias}.${propertyName}"
  }
  
  static String aliasedPropertyNameFor(Map options, property) {
    if (property instanceof AbstractPropertyExpressionBase) {
      property.propertyFor(options)
    } else {
      fullPropertyNameFor(options, property)
    }
  }
  
  def propertyFor(options) {
    throw new Exception("Please implement propertyFor() in the extending class.")
  }
  
  def call = { Map args ->
    builder._processArgs_(this, args)
  }
  
}