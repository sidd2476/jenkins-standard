def call() {  
  node {
    stage('Verify Workspace'){
      echo "${env.JENKINS_HOME}"
      Map pipelineConfig = readYaml(file: "${WORKSPACE}/Jenkinsfile.yaml")
    }
    stage('Build'){
      println "Building: ${pipelineConfig.project_name}"
    }
  }
}  
