package com.radiadesign.relationalscope

import org.hibernate.criterion.*
import org.codehaus.groovy.grails.commons.ApplicationHolder

class RelationalScope {
  
  static sessionFactory
  
  Class domain
  def scopes = []
  
  def results
  
  // --------------------------------------------------------------------------
  // Constructors
  // --------------------------------------------------------------------------
  
  RelationalScope(Class _domain) {
    domain = _domain
  }
  
  private RelationalScope(Class _domain, ArrayList _scopes) {
    // Provides a deep copy of the stored scopes to ensure thread safety
    scopes = _scopes.clone()
    domain = _domain
  }
  
  
  // --------------------------------------------------------------------------
  // Public API
  // --------------------------------------------------------------------------
  
  def where(RelationalScope additionalScope) {
    def newScope = this.clone()
    newScope.addScopeOrComparison(additionalScope)
    return newScope
  }
  
  def where(Closure block) {
    def builder = new RelationalScopeBuilder(domain)
    block.delegate = builder
    block.resolveStrategy = Closure.DELEGATE_FIRST
    block.call()
    return this.where(block.relationalScope)
  }
  
  
  // --------------------------------------------------------------------------
  // Protected API (can be used by this package, should not be used externally)
  // --------------------------------------------------------------------------
  
  def addScopeOrComparison(additionalScope) {
    scopes << additionalScope
  }
  
  
  // --------------------------------------------------------------------------
  // Private API
  // --------------------------------------------------------------------------
  
  def executeQuery() {
    def session = sessionFactory.currentSession
    def criteria = session.createCriteria(domain).add(this.toCriterion())
    results = criteria.list()
  }
  
  Criterion toCriterion() {
    if (scopes.size() == 1) {
      // If there is only one scope contained in this object then
      // just delegate to that scope.
      return scopes.first().toCriterion()
    } else if (scopes.size() > 1) {
      // The default combination strategy of multiple scopes is AND.
    } else {
      // There is no criteria to create.
      return null
    }
  }
  
  // Provides a thread-safe copy of the current RelationalScope
  RelationalScope clone() {
    return new RelationalScope(domain, scopes)
  }
  
}