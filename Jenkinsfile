#!/usr/bin/env groovy
def gv
pipeline {
    agent any 
    tools {
        nodejs 'node24'
    }
    parameters {
        choice(name: 'VERSION_BUMP', choices: ['minor', 'patch', 'major'], description: 'Which part of the version to increment')
    }
    stages {
        stage("init") {
            steps {
                script {
                    gv = load "script.groovy"
                }
            }
        }
        stage("incrementVersion") {
            steps {
                script {
                    gv.incrementVersion()
                }
            }
        }
        stage ("testImage") {
            steps {
                script {
                   gv.testImage() 
                }
            }
        }
        stage ("buildImage") {
            steps {
                script {
                    gv.buildImage()
                }
            }
        }
        stage ("pushImage") {
            steps {
                script {
                   gv.pushImage()
                }
            }
        }
        stage ("commitToGit") {
            steps {
                script {
                    gv.commitToGit()
                }
            }
        }
    }
}

// test it