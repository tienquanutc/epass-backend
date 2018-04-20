package model

import groovy.transform.builder.Builder

class User {
    String databaseId
    String username
    String hashPassword
    String salt
    Date createdAt
    Date updatedAt

    User databaseId(String databaseId) {
        this.databaseId = databaseId
        return this
    }

    User username(String username) {
        this.username = username
        return this
    }

    User hashPassword(String hashPassword) {
        this.hashPassword = hashPassword
        return this
    }

    User salt(String salt) {
        this.salt = salt
        return this
    }

    User createdAt(Date createdAt) {
        this.createdAt = createdAt
        return this
    }

    User updatedAt(Date updatedAt) {
        this.updatedAt = updatedAt
        return this
    }
}
