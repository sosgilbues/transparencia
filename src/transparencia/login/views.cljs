(ns transparencia.login.views
  (:require
    [re-frame.core :as re-frame]
    [transparencia.login.subs :as subs]
    [transparencia.login.events :as events]
    [transparencia.routes :as routes]))

(defn login-panel []
  (when-let [access-token @(re-frame/subscribe [::subs/access-token])]
    (.setItem (.-localStorage js/window) "access-token" access-token)
    (routes/navigate! :manage))

  [:div.container
   [:h1.title "Login"]

   (let [login-form (re-frame/subscribe [::subs/login-form])
         login-result (re-frame/subscribe [::subs/login-result])]
     [:div.container

      [:div.field
       [:label.label "Login"]
       [:div.control
        [:input.input {:type      "text"
                       :on-change #(re-frame/dispatch [::events/update-form :username (-> % .-target .-value)])
                       :value     (:username @login-form)}]]]

      [:div.field
       [:label.label "Senha"]
       [:input.input {:type      "password"
                      :on-change #(re-frame/dispatch [::events/update-form :password (-> % .-target .-value)])
                      :value     (:password @login-form)}]]

      [:button.button {:on-click #(re-frame/dispatch [::events/login])} "Entrar"]

      (when (= @login-result :invalid-credentials)
        [:section.section
         [:div.notification.is-danger
          "Nome de usuário ou senha inválidos."]])])])

(defmethod routes/panels :login-panel [] [login-panel])
