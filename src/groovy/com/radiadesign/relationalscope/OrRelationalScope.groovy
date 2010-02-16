package com.radiadesign.relationalscope

import org.hibernate.criterion.*

class OrRelationalScope extends RelationalScope {
  
  // --------------------------------------------------------------------------
  // Constructors
  // --------------------------------------------------------------------------
  
  OrRelationalScope() {
    super()
  }
  
  OrRelationalScope(Class _domain) {
    super(_domain)
  }
  
  //OrRelationalScope(JSONObject json) {
  //  super(json)
  //}
  
  private OrRelationalScope(Class _domain, ArrayList _scopes) {
    super(_domain, _scopes)
  }
  
  
  // --------------------------------------------------------------------------
  // Private API
  // --------------------------------------------------------------------------
  
  def instance() {
    new OrRelationalScope()
  }
  
  def junction() {
    Restrictions.disjunction()
  }
  
}