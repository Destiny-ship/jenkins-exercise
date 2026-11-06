#!/usr/bin/env groovy
@Library('node-shared-library') _

pipeline {
    agent any
    tools {
        nodejs 'node24'
    }
    parameters {
        choice(name: 'VERSION_BUMP', choices: ['minor', 'patch', 'major'], description: 'Which part of the version to increment')
    }
    stages {
        stage("incrementVersion") {
            steps {
                incrementVersion()
            }
        }
        stage("testCode") {
            steps {
                testCode()
            }
        }
        stage("buildImage") {
            steps {
                buildImage "destinyship/dev-projects-app:${env.IMAGE_NAME}"
            }
        }
        stage("pushImage") {
            steps {
                pushImage "destinyship/dev-projects-app:${env.IMAGE_NAME}", 'docker-hub-repo'
            }
        }
        stage("commit") {
            steps {
                commitToGit 'github-cred', 'github.com/Destiny-ship/jenkins-exercise.git'
            }
        }
    }
}