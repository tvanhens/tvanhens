(ns tvanhens.entity
  "Simple entity modeling, useful for following clean architecture principles"
  (:require [clojure.spec.alpha :as s]))

(defn- ns-entity-spec []
  (let [ns-name (str *ns*)]
    (->> ns-name
         (re-find #"^.*\.(.*)$")
         second
         (keyword ns-name))))

;;------------------------------------------------------------------------------
;; API:

(defn wrapped? [data] (boolean (::spec data)))

(defn validate! [entity]
  (assert (wrapped? entity))
  (let [spec (::spec entity)]
    (when-not (s/valid? spec entity)
      (throw (ex-info "invalid-entity"
                      {:spec    (s/form spec)
                       :entity  entity
                       :explain (s/explain-data spec entity)}))))
  entity)

(defn wrap
  ([spec data] (wrap spec data nil))
  ([spec data create?]
   (let [data' (assoc data ::spec spec)]
     (-> data'
         (cond-> create? (assoc ::changes [(keyword (str *ns*) "create")
                                           nil
                                           nil
                                           data']))
         validate!))))

(defn changes [entity] (::changes entity))

(defmacro defaccess [accessor-name fn]
  (let [ns-spec (ns-entity-spec)]
    `(defn ~accessor-name [~'entity ~'& ~'opts]
       (assert (= (::spec ~'entity) ~ns-spec))
       (validate! ~'entity)
       (apply ~fn ~(keyword accessor-name) ~'entity ~'opts))))

(defmacro defchange [change-name fn]
  (let [ns-spec (ns-entity-spec)]
    `(defn ~change-name [~'entity ~'& ~'opts]
       (assert (= (::spec ~'entity) ~ns-spec))
       (validate! ~'entity)
       (let [~'next (apply ~fn ~(keyword change-name) ~'entity ~'opts)]
         (-> ~'next
             (update ::changes conj
                     [~(keyword (str *ns*) (name change-name))
                      ~'opts
                      (dissoc ~'entity ::changes)
                      (dissoc ~'next ::changes)]))))))

(defn simple-lookup [field entity] (field entity))

(comment
  (defaccess email simple-lookup)

  (defchange change-email (fn [_ entity email] (assoc entity :email email)))

  (s/def ::email string?)

  (s/def ::entity (s/keys :req-un [::email])))
