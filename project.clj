(defproject tvanhens "0.2.11-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-beta1"]]
  :lein-release {:deploy-via :clojars}
  :plugins [[lein-codox            "0.10.3"]
            [lonocloud/lein-unison "0.1.13"]]
  :release-tasks
  [["vcs" "assert-committed"]
   ["codox"]
   ["change" "version" "leiningen.release/bump-version" "release"]
   ["vcs" "commit"]
   ["vcs" "tag" "--no-sign"]
   ["deploy"]
   ["unison" "release-projects" "tvanhens"]
   ["change" "version" "leiningen.release/bump-version"]
   ["vcs" "commit"]]
  :unison
  {:repos
   [{:git            "git@github.com:tvanhens/track.git"
     :release-script "script/release.sh"
     :branch         "master"}]}
  :codox {:output-path "docs"}
  :deploy-repositories
  {"releases" {:url           "https://clojars.org/repo"
               :sign-releases false
               :username      "tvanhens"
               :password      :env/CLOJARS_PASSWORD}})
