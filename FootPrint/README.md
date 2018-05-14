# zxmvp<br>
一个封装好的mvp框架<br>
使用方法如下：<br>
1.下载项目<br>
2.拷贝zxmvp到你的项目中作为依赖<br>
3.建立BaseActivit<br>
```
public abstract class BaseActivity<T extends BasePresenter, E extends BaseModel> extends RxBaseActivity<T, E> {
    @Override
    public void showToast(String message) {
        ZXToastUtil.showToast(message);
    }

    @Override
    public void showLoading(String message) {
        ZXDialogUtil.showLoadingDialog(this, message);
    }

    @Override
    public void dismissLoading() {
        ZXDialogUtil.dismissLoadingDialog();
    }
}
```
4.同理建立baseFragment<br>
```
public abstract class BaseFragment<T extends BasePresenter, E extends BaseModel> extends RxBaseFragment<T, E>{

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void showLoading(String message) {
        ZXDialogUtil.showLoadingDialog(getActivity(), message);
    }

    @Override
    public void dismissLoading() {
        ZXDialogUtil.dismissLoadingDialog();
    }

    @Override
    public void showToast(String message) {
        ZXToastUtil.showToast(message);
    }
}
```
5.每一个类都需要，contract、model、presenter、ui、layout这几个文件，当然这是说使用mvp的情况下，如果不使用，正常方法即可<br>
```
public interface LoginContract {

    interface Model extends IModel {
        Observable<LoginEntity> loginData(Map<String, String> map);
    }

    interface View extends IView {
        void onLoginResult(LoginEntity loginResult);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void doLogin(Map<String, String> map);
    }

}
```

```
public class LoginModel extends BaseModel implements LoginContract.Model {


    @Override
    public Observable<LoginEntity> loginData(Map<String, String> map) {
        return mRepositoryManager.obtainRetrofitService(ApiService.class)
                .login(map)
                .compose(RxHelper.handleResult())
                .compose(RxSchedulers.io_main());
    }
}
```

```
public class LoginPresenter extends LoginContract.Presenter {

    public void doLogin(Map<String, String> map) {
        mModel.loginData(map)
                .flatMap(new Func1<LoginEntity, Observable<List<MapUrlEntity>>>() {
                    @Override
                    public Observable<List<MapUrlEntity>> call(LoginEntity loginEntity) {
                        mView.onLoginResult(loginEntity);
                        return mModel.mapUrlData(ApiParamUtil.getMapUrlDataInfo());
                    }
                })
                .compose(RxHelper.bindToLifecycle(mView))
                .subscribe(new RxSubscriber<List<MapUrlEntity>>(mView) {
                    @Override
                    protected void _onNext(List<MapUrlEntity> mapUrlResultList) {
                        mView.onMapUrlResutl(mapUrlResultList);
                    }

                    @Override
                    protected void _onError(String message) {
                        mView.showToast(message);
                    }
                });
    }
}
```

```
public class LoginActivity extends BaseActivity<LoginPresenter, LoginModel> implements LoginContract.View {
}
```

6.模板，我做了一套专门针对于该框架的模板，可以使用[MVPTemplates](https://github.com/StannyBing/MVPTemplate) 
