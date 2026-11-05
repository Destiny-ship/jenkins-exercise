

def incrementVersion() {
    dir('app') {
    sh "npm version ${params.VERSION_BUMP} --no-git-tag-version"
    def matcher = readFile('package.json') =~ '"version": "(.+)"'
    def version = matcher[0][1]
    env.IMAGE_NAME = "$version-$BUILD_NUMBER"
    }
}

def testImage() {
dir('app') {
    sh 'npm ci'
    sh 'npm test'
    }
}

def buildImage() {
    dir('app') {
    sh 'docker build --platform linux/amd64 -t destinyship/dev-projects-app:${IMAGE_NAME} .'
    }
}

def pushImage() {
     withCredentials([usernamePassword(credentialsId: 'docker-hub-repo', usernameVariable: 'USER', passwordVariable: 'PASS')]) {
        sh 'echo $PASS | docker login -u $USER --password-stdin'
        sh 'docker push destinyship/dev-projects-app:${IMAGE_NAME}'
    }
}

def commitToGit() {
    withCredentials([usernamePassword(credentialsId: 'github-cred', usernameVariable: 'USER', passwordVariable: 'PASS')]) {
        sh 'git config user.email "jenkins@jenkins.com"'
        sh 'git config user.name "jenkins"'
        sh 'git add .'
        sh 'git commit -m "ci: version bump"'
        sh 'git remote set-url origin https://$USER:$PASS@github.com/Destiny-ship/jenkins-exercise.git'
        sh 'git push -u origin HEAD:main'
    }
}

return this
