(defproject tvanhens "0.2.5-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-beta1"]]
  :lein-release {:deploy-via :clojars}
  :plugins [[lein-release "1.0.9"]]
  :release-tasks [["vcs" "assert-committed"]
                  ["change" "version"
                   "leiningen.release/bump-version" "release"]
                  ["vcs" "commit"]
                  ["vcs" "tag" "--no-sign"]
                  ["deploy"]
                  ["file-replace" "README.md" "\\[api \"" "\"]" "version"]
                  ["change" "version" "leiningen.release/bump-version"]
                  ["vcs" "commit"]]
  :deploy-repositories
  {"clojars" {:url           "https://clojars.org/repo"
              :sign-releases false
              :username      "tvanhens"
              :password      :env/CLOJARS_PASSWORD}})
